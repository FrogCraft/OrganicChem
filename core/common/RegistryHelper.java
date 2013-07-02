package organicchem.core.common;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import organicchem.core.item.ItemTube;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.src.ModLoader;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.DimensionManager;

public class RegistryHelper {
	public static final RegistryHelper INSTANCE;
	
	public static final String TEXTUREPATH = "organicchem";
	
	private static final String DIMENSIONPREFIX = "ID.Dimension.";
	private static final String CREATIVEPAGENAME = "OrganicChem";
	public static final String CREATIVEPAGENAME_UNL = "itemGroup." + CREATIVEPAGENAME;
	
	static {
		INSTANCE = new RegistryHelper();
	}
	
	public enum RegistryType {
		Block,
		Item,
		Dimension
	}
	
	private class RegInfo {
		public int nextId;
		public Map<String, Integer> idMap;
		public RegInfo(int defStartFrom) {
			idMap = new HashMap<String, Integer>();
			nextId = defStartFrom;
		}
		public int getId(String name) {
			return idMap.get(name);
		}
		public void setDef(String name) {
			idMap.put(name, nextId++);
		}
	}
	
	private Map<RegistryType,RegInfo> regInfo;
	private Map<Class<?>, RegistryType> regInfoClassType = new HashMap();
	//private Map<Class<?>, String> regInfoClassName = new HashMap();
	//private Map<Class<?>, Integer> regInfoClassId;
	private Map<Class<?>, ArrayList<String>> regInfoClassName = new HashMap();
	private Map<Class<?>, ArrayList<Integer>> regInfoClassId;
	private Map<Class<?>, Class<? extends TileEntity>> tileEntityMap = new HashMap();//only before reg process finished
	
	private RegistryHelper() {
		regInfo = new HashMap<RegistryType,RegInfo>();
		regInfo.put(RegistryType.Block, new RegInfo(310));
		regInfo.put(RegistryType.Item, new RegInfo(14100));
		regInfo.put(RegistryType.Dimension, new RegInfo(5));
	}
	
	public static void readFromConfig() {
		try {
			RegInfo info;
			//block
			info = INSTANCE.regInfo.get(RegistryType.Block);
			for (Entry<String, Integer> i : info.idMap.entrySet()) {
				i.setValue(ConfigManager.GetBlockID(i.getKey(), i.getValue()));
			}
			
			//item
			info = INSTANCE.regInfo.get(RegistryType.Item);
			for (Entry<String, Integer> i : info.idMap.entrySet()) {
				i.setValue(ConfigManager.GetItemID(i.getKey(), i.getValue()));
			}
			
			//dimension
			info = INSTANCE.regInfo.get(RegistryType.Dimension);
			for (Entry<String, Integer> i : info.idMap.entrySet()) {
				i.setValue(ConfigManager.GetGeneralProperties(DIMENSIONPREFIX + i.getKey(), i.getValue()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int getId(RegistryType type, String name) {
		return INSTANCE.regInfo.get(type).getId(name);
	}
	
	public static void setDefId(RegistryType type, String name) {
		INSTANCE.regInfo.get(type).setDef(name);	
	}
	
	public static void setClassDefId(RegistryType type, String name, Class<?> c) {
		setDefId(type, name);
		INSTANCE.regInfoClassType.put(c, type);
		ArrayList<String> names = INSTANCE.regInfoClassName.get(c);
		if (names == null) names = new ArrayList<String>();
		names.add(name);
		INSTANCE.regInfoClassName.put(c, names);
	}
	
	public static void setItemDefId(String name, Class<?> c) {
		setClassDefId(RegistryType.Item, name, c);
	}
	
	public static void setBlockDefId(String name, Class<?> c) {
		setClassDefId(RegistryType.Block, name, c);
	}
	
	public static void setBlockDefId(String name, Class<?> block, Class<? extends TileEntity> tileEntity) {
		setClassDefId(RegistryType.Block, name, block);
		INSTANCE.tileEntityMap.put(block, tileEntity);
	}
	
	public static void setDimensionDefId(String name, Class<?> c) {
		setClassDefId(RegistryType.Dimension, name, c);
	}
	
	public static int getId(Class<?> c) {
		return INSTANCE.regInfoClassId.get(c).get(0);
	}
	
	public static int getId(Class<?> c, int index) {
		return INSTANCE.regInfoClassId.get(c).get(index);
	}
	
	public static int getItemId(Class<?> c) {
		return INSTANCE.regInfoClassId.get(c).get(0) + 256;
	}
	
	public static int getItemId(Class<?> c, int index) {
		return INSTANCE.regInfoClassId.get(c).get(index) + 256;
	}
	
	public static String getName(Class<?> c) {
		return INSTANCE.regInfoClassName.get(c).get(0);
	}

	public static String getName(Class<?> c, int index) {
		return INSTANCE.regInfoClassName.get(c).get(index);
	}
	
	public static void registerWorld(Class c, int id) {
		DimensionManager.registerProviderType(id, c, false);
		DimensionManager.registerDimension(id, id);
	}
	
	public static void registerBlock(Class c) {
		
	}
	
	public void finishLoading() {
		regInfoClassId = new HashMap();
		RegistryType type;
		int id;
		//String name;
		ArrayList<String> names;
		ArrayList<Integer> ids;
		for (Class i : regInfoClassName.keySet()) {
			type = regInfoClassType.get(i);
			names = regInfoClassName.get(i);
			ids = new ArrayList<Integer>();
			
			for (String name : names) {
				id = regInfo.get(type).getId(name);
				ids.add(id);
				
				if (type == RegistryType.Item) {
					
				} else if (type == RegistryType.Block) {
					if (tileEntityMap.containsKey(i)) {
						ModLoader.registerTileEntity((Class<? extends TileEntity>) tileEntityMap.get(i), name);
					}
				} else if (type == RegistryType.Dimension) {
					registerWorld(i, id);
				}
			}
			
			regInfoClassId.put(i, ids);
		}
		tileEntityMap.clear();
		tileEntityMap = null;
	}
	
	public void createItemsAndBlocks() {
		try {
			RegistryType type;
			Class i;
			int count;
			for (Entry<Class<?>, ArrayList<String>> e : regInfoClassName.entrySet()) {
				i = e.getKey();
				type = regInfoClassType.get(i);
				count = e.getValue().size();
				if (count == 1) {
					if (type == RegistryType.Item) {
						i.getConstructor().newInstance();
					} else if (type == RegistryType.Block) {
						ModLoader.registerBlock((Block) i.getConstructor().newInstance());
					}
				} else {
					Constructor con = i.getConstructor(Integer.class);
					for (int j = 0; j < count; ++j) {
						if (type == RegistryType.Item) {
							con.newInstance(j);
						} else if (type == RegistryType.Block) {
							ModLoader.registerBlock((Block) con.newInstance(j));
						}
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		regInfoClassType.clear();
		regInfoClassType = null;
	}
	
	public static Block getBlock(Class c) {
		return Block.blocksList[getId(c)];
	}
	
	public static Block getBlock(Class c, int index) {
		return Block.blocksList[getId(c, index)];
	}
	
	public static Item getItem(Class c) {
		return Item.itemsList[256 + getId(c)];
	}
	
	public static Item getItem(Class c, int index) {
		return Item.itemsList[256 + getId(c, index)];
	}
	
	public static CreativeTabs creativeTab = new CreativeTabs(CREATIVEPAGENAME) {
		@Override
		public Item getTabIconItem() {
			return getItem(ItemTube.class);
		}
	};
}

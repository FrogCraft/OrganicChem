package organicchem.core.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import organicchem.core.block.*;
import organicchem.core.item.*;

import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraft.item.Item;
import net.minecraft.util.StatCollector;
import net.minecraft.util.StringTranslate;

public class LanguageManager {
	public static LanguageManager INSTANCE = new LanguageManager();
	private static final String HEADER = "Language file for Organic Chem.";

	private Properties table;
	
	public static String translate(String str) {
		return StatCollector.translateToLocal(str);//StringTranslate.getInstance().translateKey(str);
	}
	
	public static void init(File langFile) {
		INSTANCE.table = new Properties();
		try {
			if (!langFile.exists()) {
				langFile.createNewFile();
			}
			FileInputStream is = new FileInputStream(langFile);
			INSTANCE.table.load(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		INSTANCE.fillTable();
		LanguageRegistry.instance().addStringLocalization(INSTANCE.table);
		try {
			FileOutputStream os = new FileOutputStream(langFile);
			INSTANCE.table.store(os, HEADER);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String forClass(Class c) {
		if (Item.class.isAssignableFrom(c)) {
			return "item." + RegistryHelper.getName(c) + ".name";
		} else {// if (Block.class.isAssignableFrom(c)) {
			return "tile." + RegistryHelper.getName(c) + ".name";
		}
	}
	
	private String forClass(Class c, int index) {
		if (Item.class.isAssignableFrom(c)) {
			return "item." + RegistryHelper.getName(c, index) + ".name";
		} else {// if (Block.class.isAssignableFrom(c)) {
			return "tile." + RegistryHelper.getName(c, index) + ".name";
		}
	}
	
	private void fillTable() {
		//Item
		table.setProperty(forClass(ItemTube.class), "试管");
		table.setProperty(forClass(ItemChemicalBottle.class), "试剂瓶");
		//Block
		table.setProperty(forClass(BlockMicroscope.class), "显微镜");
		table.setProperty(forClass(BlockStorage.class, 0), "小型存储罐");
		table.setProperty(forClass(BlockStorage.class, 1), "中型存储罐");
		table.setProperty(forClass(BlockStorage.class, 2), "大型存储罐");
		//CreativeTabs
		table.setProperty(RegistryHelper.CREATIVEPAGENAME_UNL, "OrganicChem");
	}
}

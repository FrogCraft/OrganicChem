package organicchem.core;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.MinecraftForge;

import organicchem.core.block.*;
import organicchem.core.block.tile.TileEntityStorage;
import organicchem.core.common.*;
import organicchem.core.item.*;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = "organicchem", name = "Organic Chemistry", version = "0.0.0")
@NetworkMod(clientSideRequired = true, serverSideRequired = false,
channels={NetworkManager.CHANNEL}, packetHandler = NetworkManager.class)

public class mod_OrganicChem {
	public static mod_OrganicChem INSTANCE;
	
	@Mod.Init
	public void load(FMLInitializationEvent evt) {
		INSTANCE = this;
		
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		
		RegistryHelper.INSTANCE.finishLoading();
		RegistryHelper.INSTANCE.createItemsAndBlocks();

		//reg liquids
		/*
		LiquidContainerRegistry.registerLiquid(new LiquidContainerData(
				LiquidDictionary.getOrCreateLiquid("OrgainicChemical",
					new LiquidStack(RegistryHelper.getItemId(ItemOrganicLiquid.class), LiquidContainerRegistry.BUCKET_VOLUME)), 
				new ItemStack(RegistryHelper.getItem(ItemChemicalBottle.class)),
				new ItemStack(Item.glassBottle)));
		*/

		CraftingManager.getInstance().getRecipeList().add(new RecipeWriteSmiles());
	}
	
	@Mod.PreInit
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		
		//Blocks
		RegistryHelper.setBlockDefId("Microscope", BlockMicroscope.class);
		RegistryHelper.setBlockDefId("SmallStorage", BlockStorage.class, TileEntityStorage.class);
		RegistryHelper.setBlockDefId("MediumStorage", BlockStorage.class);
		RegistryHelper.setBlockDefId("LargeStorage", BlockStorage.class);
		
		//Items
		RegistryHelper.setItemDefId("Tube", ItemTube.class);
		RegistryHelper.setItemDefId("OrganicLiquid", ItemOrganicLiquid.class);
		RegistryHelper.setItemDefId("ChemicalBottle", ItemChemicalBottle.class);

		ConfigManager.init(event.getSuggestedConfigurationFile());
		RegistryHelper.readFromConfig();
		ConfigManager.SaveConfig();
		
		LanguageManager.init(new File(event.getModConfigurationDirectory().getPath(), "OrgainicChemMod.lang"));
	}
	
	@Mod.PostInit
	public void postInit(FMLPostInitializationEvent event) {}
	
}

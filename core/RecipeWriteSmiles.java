package organicchem.core;

import java.util.Arrays;
import java.util.List;

import organicchem.core.common.RegistryHelper;
import organicchem.core.item.ItemTube;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagString;

public class RecipeWriteSmiles extends ShapelessRecipes {

	public RecipeWriteSmiles() {
		super(new ItemStack(RegistryHelper.getItem(ItemTube.class)), Arrays.asList(
				new ItemStack(RegistryHelper.getItem(ItemTube.class)),
				new ItemStack(Item.writtenBook)
				));
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting) {
		ItemStack item;
		for (int i = 0; i < par1InventoryCrafting.getSizeInventory(); ++i) {
			item = par1InventoryCrafting.getStackInSlot(i);
			if (item.getItem() == Item.writtenBook) {
				String text = ((NBTTagString)(item.stackTagCompound.getTagList("pages").tagAt(0))).data;
				ItemStack result = new ItemStack(RegistryHelper.getItem(ItemTube.class));
				ItemTube.setSmiles(result, text);
				return result;
			}
		}
		return new ItemStack(RegistryHelper.getItem(ItemTube.class));
	}
}

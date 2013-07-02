package organicchem.core.item;

import java.util.List;

import organicchem.core.common.ItemCommon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemTube extends ItemCommon {
	private static final String SMILES = "smiles";
	
	public ItemTube() {
		super(ItemTube.class);
	}
	
	public static String getSmiles(ItemStack itemStack) {
		NBTTagCompound tag = itemStack.stackTagCompound;
		if (tag == null || !tag.hasKey(SMILES)) {
			return "";
		}
		return tag.getString(SMILES);
	}

	public static void setSmiles(ItemStack itemStack, String smiles) {
		NBTTagCompound tag = itemStack.stackTagCompound;
		if (tag == null) {
			tag = new NBTTagCompound();
			itemStack.stackTagCompound = tag;
		}
		tag.setString(SMILES, smiles);
	}
	
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		String str = getSmiles(par1ItemStack);
		if (str.length() > 0)
			par3List.add(str);
	}
}

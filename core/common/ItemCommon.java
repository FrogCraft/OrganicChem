package organicchem.core.common;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import organicchem.core.common.RegistryHelper.RegistryType;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ItemCommon extends Item {
	public ItemCommon(Class<? extends ItemCommon> c) {
		super(RegistryHelper.getId(c));
		this.setUnlocalizedName(RegistryHelper.getName(c));
		this.setCreativeTab(RegistryHelper.creativeTab);
	}
	
	public ItemCommon(Class<? extends ItemCommon> c, CreativeTabs creativeTabs) {
		super(RegistryHelper.getId(c));
		this.setUnlocalizedName(RegistryHelper.getName(c));
		this.setCreativeTab(creativeTabs);
	}
	
	public ItemCommon(String name) {
		super(RegistryHelper.getId(RegistryType.Item, name));
		this.setUnlocalizedName(name);
		this.setCreativeTab(RegistryHelper.creativeTab);
	}
	
	public NBTTagCompound getOrCreateNBT(ItemStack itemStack) {
		NBTTagCompound r = itemStack.stackTagCompound;
		if (r == null) {
			r = new NBTTagCompound();
			itemStack.stackTagCompound = r;
		}
		return r;
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister par1IconRegister)
	{
		this.itemIcon = par1IconRegister.registerIcon(RegistryHelper.TEXTUREPATH + ":"
				+ RegistryHelper.getName(this.getClass()));
	}
}

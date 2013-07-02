package organicchem.core.common;

import java.lang.reflect.Constructor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import organicchem.core.mod_OrganicChem;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureStitched;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockContainerCommon extends BlockContainer {
	private static class MaterialSpaceMachine extends Material{
		public MaterialSpaceMachine() {
			super(MapColor.ironColor);
			this.setImmovableMobility();
			this.setRequiresTool();
		}
	}
	public static final Material MATERIAL = new MaterialSpaceMachine();
	protected final int index;
	
	public BlockContainerCommon(Class c, int index) {
		super(RegistryHelper.getId(c, index), MATERIAL);
		this.setCreativeTab(RegistryHelper.creativeTab);
		this.setUnlocalizedName(RegistryHelper.getName(c, index));
		this.index = index;
	}
	
	public BlockContainerCommon(Class c) {
		super(RegistryHelper.getId(c), MATERIAL);
		this.setCreativeTab(RegistryHelper.creativeTab);
		this.setUnlocalizedName(RegistryHelper.getName(c));
		index = 0;
	}
	
	public BlockContainerCommon(Class c, CreativeTabs t) {
		super(RegistryHelper.getId(c), MATERIAL);
		if (t != null) this.setCreativeTab(t);
		this.setUnlocalizedName(RegistryHelper.getName(c));
		index = 0;
	}

	
	//return 0 means not allowed to use the gui
	protected abstract int openGui(World par1World, TileEntity tile, EntityPlayer player);

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4,
			EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		if (par1World.isRemote) {
			return true;
		} else {
			TileEntity var10 = (TileEntity)par1World.getBlockTileEntity(par2, par3, par4);
			int id = openGui(par1World, var10, par5EntityPlayer);
			if (id > 0) {
				par5EntityPlayer.openGui(mod_OrganicChem.INSTANCE, id, par1World, par2, par3, par4);
			}
			return true;
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		String name = RegistryHelper.getName(this.getClass(), index);
		String texturePath;
		TextureStitched texture;
		Constructor con;
		try {
			Class c = Class.forName("ic2.core.block.BlockTextureStitched");
			con = c.getConstructor(String.class);
		
			for (int i = 0; i < 6; ++i) {
				texturePath = RegistryHelper.TEXTUREPATH + ":" + name + "." + Integer.toString(i);
				texture = new BlockTextureStitched(texturePath);//(TextureStitched) con.newInstance(texturePath);
				((TextureMap)iconRegister).setTextureEntry(texturePath, texture);
				iconList[i] = texture;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int par1, int par2) {
		return iconList[par1];
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5) {
		return iconList[par5];
	}
	
	private Icon iconList[] = new Icon[6];
}

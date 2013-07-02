package organicchem.core.block;

import organicchem.core.common.BlockContainerCommon;
import organicchem.core.common.GuiHandler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockMicroscope extends BlockContainerCommon {

	public BlockMicroscope() {
		super(BlockMicroscope.class);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}

	@Override
	protected int openGui(World par1World, TileEntity tile, EntityPlayer player) {
		return GuiHandler.BLOCKMICROSCOPE;
	}
}

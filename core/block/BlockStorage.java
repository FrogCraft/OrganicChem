package organicchem.core.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import organicchem.core.block.tile.TileEntityStorage;
import organicchem.core.common.BlockContainerCommon;

public class BlockStorage extends BlockContainerCommon {

	public BlockStorage(Integer index) {
		super(BlockStorage.class, index);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityStorage(index);
	}

	@Override
	protected int openGui(World par1World, TileEntity tile, EntityPlayer player) {
		return 0;
	}

}

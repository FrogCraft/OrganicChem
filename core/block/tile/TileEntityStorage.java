package organicchem.core.block.tile;

import net.minecraft.block.BlockFluid;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.FluidContainerRegistry;
import organicchem.core.common.RegistryHelper;
import organicchem.core.common.TileEntityFluid;
import organicchem.core.item.ItemOrganicLiquid;

public class TileEntityStorage extends TileEntityFluid {
	private static final String INVENTORY = "storage";
	private static final int STORAGE_COUNT[] = new int[] { 1, 9, 36 };
	private static final int STORAGE_MAX[] = new int[] { 5, 10, 50 };
	
	private int storageCount;
	private int storageMax;
	
	//only used by forge, must setup vars by readFromNBT later
	public TileEntityStorage() {
		super(INVENTORY, 0, 0);
		
		initTanks(FluidContainerRegistry.BUCKET_VOLUME);
	}
	
	public TileEntityStorage(int index) {
		super(INVENTORY, 0, STORAGE_COUNT[index]);
		storageCount = STORAGE_COUNT[index];
		storageMax = STORAGE_MAX[index];
		
		initTanks(FluidContainerRegistry.BUCKET_VOLUME);
	}

	@Override
	protected void onVarChanged(int id, int value) {}
	
}

package organicchem.core.block.tile;

import net.minecraft.block.BlockFluid;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;
import net.minecraftforge.liquids.LiquidTank;
import organicchem.core.common.RegistryHelper;
import organicchem.core.common.TileEntityCommon;
import organicchem.core.item.ItemOrganicLiquid;

public class TileEntityStorage extends TileEntityCommon implements ITankContainer {
	private static final String INVENTORY = "storage";
	private static final int STORAGE_COUNT[] = new int[] { 1, 9, 36 };
	private static final int STORAGE_MAX[] = new int[] { 5, 10, 50 };
	
	private int storageCount;
	private int storageMax;
	
	//only used by forge, must setup vars by readFromNBT later
	public TileEntityStorage() {
		super(INVENTORY, 0, 0);
	}
	
	public TileEntityStorage(int index) {
		super(INVENTORY, 0, STORAGE_COUNT[index]);
		storageCount = STORAGE_COUNT[index];
		storageMax = STORAGE_MAX[index];
	}

	@Override
	protected void onVarChanged(int id, int value) {

	}
	
	private ILiquidTank[] tanks = new LiquidTank[]{ new LiquidTank(LiquidContainerRegistry.BUCKET_VOLUME) };

	@Override
	public int fill(ForgeDirection from, LiquidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public int fill(int tankIndex, LiquidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public LiquidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return new LiquidStack(RegistryHelper.getItem(ItemOrganicLiquid.class), maxDrain)
				.setRenderingIcon(BlockFluid.func_94424_b("lava"));
	}

	@Override
	public LiquidStack drain(int tankIndex, int maxDrain, boolean doDrain) {
		return new LiquidStack(RegistryHelper.getItem(ItemOrganicLiquid.class), maxDrain)
				.setRenderingIcon(BlockFluid.func_94424_b("lava"));
	}

	@Override
	public ILiquidTank[] getTanks(ForgeDirection direction) {
		return tanks;
	}

	@Override
	public ILiquidTank getTank(ForgeDirection direction, LiquidStack type) {
		return tanks[0].getLiquid().isLiquidEqual(type) ? tanks[0] : null;
	}

}

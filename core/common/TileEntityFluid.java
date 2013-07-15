package organicchem.core.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public abstract class TileEntityFluid extends TileEntityCommon implements IFluidHandler {
	private static final String NBT_FLUID = "fluid";
	
	public TileEntityFluid(String name, int inventorySize, int varCount) {
		super(name, inventorySize, varCount);
	}

	protected void initTanks(int... i) {
		
	}

	protected FluidTank[] tanks;
	
	
	@Override
	public int fill(ForgeDirection from, FluidStack res, boolean doFill) {
		FluidStack resource = res.copy();
		if (tanks == null) return 0;
		int left = resource.amount;
		for (int i = 0; i < tanks.length; ++i) {
			resource.amount -= tanks[i].fill(resource, doFill);
		}
		return res.amount - resource.amount ;
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		if (tanks == null || resource == null) return null;
		FluidStack r = resource.copy();
		FluidStack t;
		for (int i = 0; i < tanks.length; ++i) {
			t = tanks[i].drain(r.amount , doDrain);
			if (t != null) r.amount -= t.amount;
		}
		r.amount = resource.amount - r.amount;
		return r;
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		if (tanks == null || tanks.length == 0) return null;
		FluidStack res = tanks[0].getFluid().copy();
		res.amount = maxDrain;
		drain(from, res, doDrain);
		return null;
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return tanks != null;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return tanks != null;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		if (tanks == null) return null;
		FluidTankInfo[] ret = new FluidTankInfo[tanks.length];
		for (int i = 0; i < tanks.length; ++i) {
			ret[i] = new FluidTankInfo(tanks[i]);
		}
		return ret;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		try {
			NBTTagList list = nbt.getTagList(NBT_FLUID);
			for (int i = 0; i < tanks.length; ++i) {
				tanks[i].readFromNBT((NBTTagCompound) list.tagAt(i));
			}
		} catch (IndexOutOfBoundsException e) {}
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagList list = new NBTTagList();
		NBTTagCompound tank;
		for (int i = 0; i < tanks.length; ++i) {
			tank = new NBTTagCompound();
			tanks[i].writeToNBT(tank);
			list.appendTag(tank);
		}
		nbt.setTag(NBT_FLUID, list);
	}
}

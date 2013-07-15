package organicchem.core.common;

import ic2.api.tile.IWrenchable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityCommon extends TileEntity implements IInventory, ISidedInventory, IWrenchable {

	public static final String INVENTORY = "inventory";
	private static final String SLOT = "slot";
	private static final String FACING = "facing";
	private static final String VARS = "vars";
	
	protected InventoryBasic inventory;
	protected int[] availableSide;
	private String name;
	
	//IWrenchable
	private boolean wrenchEnabled = false;
	private int facing = 5;
	
	//vars
	protected int[] vars;
	
	public TileEntityCommon(String name, int inventorySize, int varCount) {
		this.name = name;
		
		inventory = new InventoryBasic(name, false, inventorySize);
		availableSide = new int[inventorySize];
		for (int i = 0; i < inventorySize; ++i) {
			availableSide[i] = i;
		}
		
		vars = new int[varCount];
	}
	
	protected void setVarCount(int count) {
		vars = new int[count];
	}
	
	@Override
	public int getSizeInventory() {
		return inventory.getSizeInventory();
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return inventory.getStackInSlot(i);
	}

	@Override
	public ItemStack decrStackSize(int i, int j) {
		return inventory.decrStackSize(i, j);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		inventory.setInventorySlotContents(i, itemstack);
	}

	@Override
	public String getInvName() {
		return name;
	}

	@Override
	public boolean isInvNameLocalized() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return true;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return availableSide;
	}

	@Override
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		return true;
	}

	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		return true;
	}

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readFromNBT(par1NBTTagCompound);
		readInventoryFromNBT(par1NBTTagCompound.getTagList(INVENTORY), inventory);
		int [] varsNBT = par1NBTTagCompound.getIntArray(VARS);
		facing = par1NBTTagCompound.getShort(FACING);
		if (vars.length == varsNBT.length) {
			vars = varsNBT;
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		NBTTagList list = new NBTTagList();
		writeInventoryToNBT(list, inventory);
		par1NBTTagCompound.setTag(INVENTORY, list);
		par1NBTTagCompound.setShort(FACING, (short) facing);
		par1NBTTagCompound.setIntArray(VARS, vars);
	}
	
	public static void readInventoryFromNBT(NBTTagList nbt, IInventory inventory) {
		int count = inventory.getSizeInventory();
		int slot;
		NBTTagCompound item;
		ItemStack itemStack;
		for (int i = 0; i < count; ++i) {
			item = (NBTTagCompound) nbt.tagAt(i);
			slot = item.getByte(SLOT);
			itemStack = ItemStack.loadItemStackFromNBT(item);
			if (itemStack != null) {
				inventory.setInventorySlotContents(slot, itemStack);
			}
		}
	}
	
	public static void writeInventoryToNBT(NBTTagList nbt, IInventory inventory) {
		int count = inventory.getSizeInventory();
		NBTTagCompound item;
		ItemStack itemStack;
		for (int i = 0; i < count; ++i) {
			item = new NBTTagCompound();
			itemStack = inventory.getStackInSlot(i);
			if (itemStack != null) {
				item.setByte(SLOT, (byte) i);
				itemStack.writeToNBT(item);
			}
			nbt.appendTag(item);
		}
	}
	

	protected void setWrenchEnabled(boolean value) {
		this.wrenchEnabled = value;
	}
	
	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
		return wrenchEnabled && side != facing;
	}

	@Override
	public short getFacing() {
		return (short) facing;
	}

	@Override
	public void setFacing(short facing) {
		this.facing = facing; 
	}

	@Override
	public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
		return false;
	}

	@Override
	public float getWrenchDropRate() {
		return 1.0f;
	}

	@Override
	public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
		return new ItemStack(this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord), 1,
				this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord));
	}
	
	protected abstract void onVarChanged(int id, int value);
	
	public void setVar(int id, int value) {
		if (vars[id] == value) return;
		vars[id] = value;
		onVarChanged(id, value);
	}
	
	public int getVar(int id) {
		return vars[id];
	}
}

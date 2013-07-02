package organicchem.core.block.gui;

import organicchem.core.chem.Graph;
import organicchem.core.chem.NativeWrapper;
import organicchem.core.common.ContainerCommon;
import organicchem.core.item.ItemTube;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerMicroscope extends ContainerCommon {
	public static final String INVENTORY = "container.spaceworkbench";
	private InventoryCrafting inventory = new InventoryCrafting(this, 1, 1);
	public Graph graph;

	public ContainerMicroscope(World world, EntityPlayer player, int x, int y, int z) {
		super(world, player, x, y, z);
		this.addPlayerSlots(player);
		this.addSlotToContainer(new Slot(inventory, 0, 8, 9));
	}

	@Override
	public void onGuiEvent(int param) {}

	@Override
	public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
		super.onCraftGuiClosed(par1EntityPlayer);
		if (!this.world.isRemote) {
			ItemStack item = inventory.getStackInSlot(0);
			if (item != null) {
				par1EntityPlayer.dropPlayerItem(item);
			}
		}
	}
	
	@Override
	public void onCraftMatrixChanged(IInventory par1IInventory) {
		ItemStack item = par1IInventory.getStackInSlot(0);
		if (item == null || ! (item.getItem() instanceof ItemTube) ) {
			graph = null;
			return;
		}
		long molecule = NativeWrapper.smilesToMolecule(ItemTube.getSmiles(item));
		if (molecule > 0) {
			int graphData[] = NativeWrapper.drawMolecule(molecule);
			graph = new Graph(graphData);
			NativeWrapper.releaseMolecule(molecule);
		} else {
			graph = null;
		}
	}
}

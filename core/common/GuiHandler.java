package organicchem.core.common;

import organicchem.core.block.gui.ContainerMicroscope;
import organicchem.core.block.gui.GuiMicroscope;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	public static final int BLOCKMICROSCOPE = 13;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case BLOCKMICROSCOPE:
			return new ContainerMicroscope(world, player, x, y, z);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
		case BLOCKMICROSCOPE:
			return new GuiMicroscope(world, player, x, y, z);
		}
		return null;
	}

}

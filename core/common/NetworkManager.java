package organicchem.core.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class NetworkManager implements IPacketHandler {
	public static final String CHANNEL = "orgchem";
	public static final int CONTAINER = 1;	//Server <---> Client
	public static final int GUIEVENT = 2;	//Server <---- Client
	
	public static void sendPlayerMessage(EntityPlayer player, String text) {
		if (player instanceof EntityPlayerMP) {
			((EntityPlayerMP) player).sendChatToPlayer(text);
		} else if (player instanceof EntityPlayerSP) {
			((EntityPlayerSP) player).addChatMessage(text);
		}
	}

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if (packet instanceof PacketContainerEvent) {
			PacketContainerEvent containerPacket = (PacketContainerEvent) packet;
			containerPacket.readData();
			if (containerPacket.type == CONTAINER) {
				EntityPlayer entityPlayer;
				Side side = FMLCommonHandler.instance().getEffectiveSide();
				if (side == Side.CLIENT) {
					entityPlayer = Minecraft.getMinecraft().thePlayer;
				} else {
					entityPlayer = MinecraftServer.getServer().
						getConfigurationManager().getPlayerForUsername(containerPacket.player);
				}
				ContainerCommon container = (ContainerCommon) entityPlayer.openContainer;
				container.onNetworkEvent((PacketContainerEvent) packet);
			} else if (containerPacket.type == GUIEVENT) {
				ContainerCommon container = (ContainerCommon) MinecraftServer.getServer().
						getConfigurationManager().getPlayerForUsername(containerPacket.player).openContainer;
				container.onGuiEvent(containerPacket.param);
			}
		}
	}

}

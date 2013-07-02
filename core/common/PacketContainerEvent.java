package organicchem.core.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketContainerEvent extends Packet250CustomPayload {
	public String player;
	public int type;
	public int param;
	
	protected PacketContainerEvent(byte[] data) {
		super(NetworkManager.CHANNEL, data);
	}

	public static PacketContainerEvent createPacket(EntityPlayer par1Player, int type, int param) {
		try {
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			DataOutputStream os = new DataOutputStream(buffer);
			os.writeUTF(par1Player.username);
			os.writeInt(type);
			os.writeInt(param);
			os.close();
			return new PacketContainerEvent(buffer.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void readData() {
		try {
			ByteArrayInputStream buffer = new ByteArrayInputStream(data);
			DataInputStream is = new DataInputStream(buffer);
			player = is.readUTF();
			type = is.readInt();
			param = is.readInt();
			is.close();
			buffer.close();
		} catch (IOException e) {
            throw new RuntimeException(e);
		}
	}

}

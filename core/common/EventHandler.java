package organicchem.core.common;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class EventHandler {
	@ForgeSubscribe
	public void onTextureStitchPost(TextureStitchEvent.Post event) {
		BlockTextureStitched.onPostStitch();
	}
}

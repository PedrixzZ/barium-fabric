package pedrixzz.barium.client;

import net.fabricmc.api.ClientModInitializer;
import pedrixzz.barium.client.render.MinecraftRenderer;

public class BariumClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		
		MinecraftRenderer.registerMinecraftRenderer();
	}
}

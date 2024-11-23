package net.dzultra.chunkchecking;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ChunkCheckingClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		var listener = new ChunkEntryListener();
		ClientTickEvents.END_CLIENT_TICK.register(listener::onEndClientTick);
		ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
			dispatcher.register(ChunkCheckingCommand.getCommand());
		}));
	}
}
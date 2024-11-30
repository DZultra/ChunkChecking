package net.dzultra.chunkchecking;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;

public class ChunkCheckingClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		var chatEventListener = new ChatEventListener();
		var chunkEventListener = new ChunkEntryListener();
		ClientTickEvents.END_CLIENT_TICK.register(chunkEventListener::onEndClientTick);
		ClientReceiveMessageEvents.ALLOW_GAME.register(chatEventListener::onGameMessage);
		ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
			dispatcher.register(ChunkCheckingCommand.getCommand());
		}));
	}
}
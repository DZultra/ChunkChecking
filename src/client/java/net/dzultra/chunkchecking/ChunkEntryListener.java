package net.dzultra.chunkchecking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ChunkPos;

public class ChunkEntryListener {
    private ChunkPos lastChunkPos = null;

    public static final Text ENTERING_NEW_CHUNK_MESSAGE_KEY = Text.translatable("message.chunkchecking.entering_new_chunk_message");


    public void onEndClientTick(MinecraftClient client) {
        if (!ChunkCheckingCommand.isRunning() || client.player == null) {
            return;
        }

        ChunkPos currentChunkPos = new ChunkPos(client.player.getBlockPos());

        if (!currentChunkPos.equals(lastChunkPos)) {
            onPlayerEnterNewChunk(currentChunkPos);

            lastChunkPos = currentChunkPos;
        }

    }

    private void onPlayerEnterNewChunk(ChunkPos newChunkPos) {
        MutableText chatmessage = Text.literal("\n").append(ENTERING_NEW_CHUNK_MESSAGE_KEY).append(Text.literal(" (" + newChunkPos.x + ", " + newChunkPos.z + ")\n"));
        MinecraftClient.getInstance().player.sendMessage(chatmessage.setStyle(Style.EMPTY.withColor(Formatting.GREEN)), false);
        MinecraftClient.getInstance().getNetworkHandler().sendChatCommand("checkchunk");
    }
}

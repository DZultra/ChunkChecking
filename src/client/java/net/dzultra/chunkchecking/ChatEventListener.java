package net.dzultra.chunkchecking;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ChatEventListener {

    public static final Text ERROR_IN_WRITING_PROCESS_MESSAGE_KEY = Text.translatable("message.chunkchecking.error_in_writing_process_message");
    public static final String LOGGER_ERROR_MESSAGE = "Error in Writing Process: currentChunkData or currentCoordinates are null";
    public DataBaseManager.ChunkCoordinates currentCoordinates = null;
    public ChunkData currentChunkData = null;

    public static int received_chunk_command = 0;
    final String baseTemplate = ": .* \\(([0-9]*)\\/([0-9]*)\\)";
    List<String> checkers = new ArrayList<>(List.of(
            "Spawners",
            "Chests",
            "Pistons",
            "Sticky Pistons",
            "Observers",
            "Hoppers",
            "Dispensers",
            "Droppers",
            "Wither SKeleton",
            "Boat",
            "Minecart",
            "Hopper Minecart",
            "Wither",
            "Villagers",
            "Axolotl"
    ));

    public boolean onGameMessage(Text message, boolean b) {
        if (ChunkCheckingCommand.isRunning()) {
            var headerMessageMatcher = Pattern.compile("\\[Foxcraft\\] Results for your current chunk \\((-?[0-9]*), (-?[0-9]*)\\):").matcher(message.getString());

            if (headerMessageMatcher.matches()) {
                if (currentChunkData != null || currentCoordinates != null) {
                    sendErrorMessageAndReset();
                    return false;
                }
                received_chunk_command = checkers.size();
                int chunkCordX = 0;
                int chunkCordZ = 0;
                try {
                    chunkCordX = Integer.parseInt(headerMessageMatcher.group(1));
                    chunkCordZ = Integer.parseInt(headerMessageMatcher.group(2));
                } catch (NumberFormatException e) {
                    sendErrorMessageAndReset();
                    throw new RuntimeException(e);
                }
                currentCoordinates = new DataBaseManager.ChunkCoordinates(chunkCordX, chunkCordZ);
                currentChunkData = new ChunkData();
                return false;
            }
            if (received_chunk_command > 0) {
                if (currentChunkData == null || currentCoordinates == null) {
                    sendErrorMessageAndReset();
                    return false;
                }
                String checker = checkers.get(checkers.size() - received_chunk_command);
                String currentRegex = checker + baseTemplate;
                received_chunk_command--;
                var matcher = Pattern.compile(currentRegex).matcher(message.getString());
                if (!matcher.matches()) return true;

                int found = 0;
                int max = 0;
                try {
                    found = Integer.parseInt(matcher.group(1));
                    max = Integer.parseInt(matcher.group(2));
                } catch (NumberFormatException e) {
                    sendErrorMessageAndReset();
                    throw new RuntimeException(e);
                }

                ChunkChecking.LOGGER.info("Found: {}/{}", found, max);

                ChunkData.DataEntry entry = new ChunkData.DataEntry(found, max);
                currentChunkData.data.put(checker, entry);
                return false;
            } else {
                if(currentChunkData != null && currentCoordinates != null) {
                    DataBaseManager.getInstance().addDataEntry(currentCoordinates, currentChunkData);
                }
                currentCoordinates = null;
                currentChunkData = null;
            }

        }
        return true; // True -> Message is shown in chat | False -> Isn't

    }

    public void sendErrorMessageAndReset() {
        MutableText chat_error_message = Text.literal("\n").append(ERROR_IN_WRITING_PROCESS_MESSAGE_KEY).append(Text.literal("\n"));
        MinecraftClient.getInstance().player.sendMessage(chat_error_message.setStyle(Style.EMPTY.withColor(Formatting.GREEN)), false);
        ChunkChecking.LOGGER.info(LOGGER_ERROR_MESSAGE);
        currentCoordinates = null;
        currentChunkData = null;
        received_chunk_command = 0;
    }
}

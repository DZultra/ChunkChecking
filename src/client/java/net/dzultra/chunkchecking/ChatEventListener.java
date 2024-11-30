package net.dzultra.chunkchecking;

import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ChatEventListener {

    public static int received_chunk_command = 0;
    final String baseTemplate = ": .* \\(([0-9]*)\\/([0-9]*)\\)";
    List<String> checkers = new ArrayList<>(List.of("Spawners", "Chests", "Pistons", "Sticky Pistons", "Observers", "Hoppers", "Dispensers", "Droppers", "Wither SKeleton", "Boat", "Minecart", "Hopper Minecart", "Wither", "Villagers", "Axolotl"));

    public boolean onGameMessage(Text message, boolean b) {
        if (ChunkCheckingCommand.isRunning()) {
            if (message.getString().matches("\\[Foxcraft\\] Results for your current chunk \\(-?[0-9]*, -?[0-9]*\\):")) {
                received_chunk_command = checkers.size();
                return false;
            }
            if (received_chunk_command > 0) {
                String currentRegex = checkers.get(checkers.size() - received_chunk_command) + baseTemplate;
                received_chunk_command--;
                var matcher = Pattern.compile(currentRegex).matcher(message.getString());
                if (!matcher.matches()) return true;
                ChunkChecking.LOGGER.info("Found: {}/{}", matcher.group(1), matcher.group(2));
                return false;
            }

        }
        return true; // True -> Message is shown in chat | False -> Isn't

    }
}

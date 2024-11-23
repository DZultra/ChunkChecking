package net.dzultra.chunkchecking;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChunkCheckingCommand {
    public static boolean isRunning() {
        return isRunning;
    }

    private static boolean isRunning = false;

    public static final LiteralArgumentBuilder<FabricClientCommandSource> COMMAND = ClientCommandManager.literal("checker");
    public static final LiteralArgumentBuilder<FabricClientCommandSource> ENABLE_NODE = ClientCommandManager.literal("enable");
    public static final LiteralArgumentBuilder<FabricClientCommandSource> DISABLE_NODE = ClientCommandManager.literal("disable");

    public static LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return COMMAND
                .then(ENABLE_NODE)
                .then(DISABLE_NODE);
    }

    static {
        ENABLE_NODE.executes(context -> {
            if (isRunning) {
                MutableText message = Text.translatable("message.chunkchecking.enable_note.already_enabled");
                MinecraftClient.getInstance().player.sendMessage(Text.literal("\n").append(message).append(Text.literal("\n"))
                        .setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)), false);
                return 1;
            }
            MutableText message = Text.translatable("message.chunkchecking.enable_note.enabling");
            MinecraftClient.getInstance().player.sendMessage(Text.literal("\n").append(message).append(Text.literal("\n"))
                    .setStyle(Style.EMPTY.withColor(Formatting.GREEN).withBold(true)), false);
            isRunning = true;
            return 1;
        });
    }

    static {
        DISABLE_NODE.executes(context -> {
            if (!isRunning) {
                MutableText message = Text.translatable("message.chunkchecking.enable_note.already_disabled");
                MinecraftClient.getInstance().player.sendMessage(Text.literal("\n").append(message).append(Text.literal("\n"))
                        .setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)), false);
                return 1;
            }
            MutableText message = Text.translatable("message.chunkchecking.enable_note.disabling");
            MinecraftClient.getInstance().player.sendMessage(Text.literal("\n").append(message).append(Text.literal("\n"))
                    .setStyle(Style.EMPTY.withColor(Formatting.GREEN).withBold(true)), false);
            isRunning = false;
            return 1;
        });
    }
}
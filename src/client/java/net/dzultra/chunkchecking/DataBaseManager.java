package net.dzultra.chunkchecking;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class DataBaseManager {
    private static DataBaseManager INSTANCE;

    public static DataBaseManager getInstance() {
        if (INSTANCE != null) return INSTANCE;
        INSTANCE = new DataBaseManager();
        return INSTANCE;
    }

    private final HashMap<ChunkCoordinates, ChunkData> collectedChunkData = new HashMap<>();

    Path baseDirectory = Path.of(MinecraftClient.getInstance().runDirectory.getAbsolutePath(), "chunkchecking");

    private DataBaseManager() {
        try {
            if (!Files.exists(baseDirectory)) {
                Files.createDirectory(baseDirectory);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while creating chunkchecking directory.", e);
        }
    }

    public void writeInFile() {
        for(ChunkCoordinates coordinate : collectedChunkData.keySet()) {
            ChunkData data = collectedChunkData.get(coordinate);
            writeSpecificFile(coordinate, data);
        }
    }

    private void writeSpecificFile(ChunkCoordinates chunkCoordinates, ChunkData chunkData) {
        Path filePath = baseDirectory.resolve("chunk_" + chunkCoordinates.x + "_" + chunkCoordinates.z + ".json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(chunkData);

        try {
            if(!Files.exists(baseDirectory)) {
                Files.createDirectory(baseDirectory);
            }
            if(!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
            Files.writeString(filePath, jsonString);
        } catch (IOException e) {
            throw new RuntimeException("Error while writing to file: " + filePath, e);
        }
    }

    public void addDataEntry(ChunkCoordinates chunkCoordinates, ChunkData chunkData) {
        collectedChunkData.put(chunkCoordinates, chunkData);
    }

    public record ChunkCoordinates(int x, int z) {}
}

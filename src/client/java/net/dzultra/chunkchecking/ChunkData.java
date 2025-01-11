package net.dzultra.chunkchecking;

import java.util.HashMap;

public class ChunkData {
    public record DataEntry(int found, int max) {}

    HashMap<String, DataEntry> data = new HashMap<>();
}

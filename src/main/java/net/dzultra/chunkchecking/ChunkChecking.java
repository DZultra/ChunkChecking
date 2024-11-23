package net.dzultra.chunkchecking;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChunkChecking implements ModInitializer {
	public static final String MOD_ID = "chunkchecking";


	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("ChunkChecking Initialized");
	}
}
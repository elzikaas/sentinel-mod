package me.sentinel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;

public class Sentinel implements ModInitializer {
    public static final String MOD_ID = "sentinel";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Sentinel Mod");
    }
}
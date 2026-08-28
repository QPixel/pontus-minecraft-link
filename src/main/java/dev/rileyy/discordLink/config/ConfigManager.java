package dev.rileyy.discordLink.config;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import dev.rileyy.discordLink.Util;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path configDir = FabricLoader.getInstance().getConfigDir();
    private static final File configFile = configDir.resolve("discord-link-config.json").toFile();

    private static DiscordLinkConfig currentConfig = new DiscordLinkConfig();

    public static DiscordLinkConfig getConfig() {
       return currentConfig;
    }


    public static void readConfig() {
        if (!configFile.exists()) {
            writeConfig();
            return;
        }
        try (FileReader reader = new FileReader(configFile)) {
            currentConfig = GSON.fromJson(reader, DiscordLinkConfig.class);
            if (currentConfig == null) {
                currentConfig = new DiscordLinkConfig();
            }
        } catch (IOException e) {
            Util.LOGGER.error(e.getMessage());
        }
    }

    public static void writeConfig() {
        try {
            boolean _ = configFile.getParentFile().mkdirs();

            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(currentConfig, writer);
            }
        } catch (IOException e) {
            Util.LOGGER.error(e.getMessage());
        }
    }
}

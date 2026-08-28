package dev.rileyy.discordLink.providers;

import net.minecraft.server.MinecraftServer;

public class ServerStatusProvider {
    private MinecraftServer server;

    public ServerStatusProvider(MinecraftServer s) {
        this.server = s;
    }

    public String[] getPlayerList() {
        return server.getPlayerNames();
    }

    public Integer getPlayerCount() {
        return server.getPlayerCount();
    }

    public String getMOTD() {
        return server.getMotd();
    }

    public String getMCVersion() {
        return server.getServerVersion();
    }

    public Integer getMaxPlayerCount() {
        return server.getMaxPlayers();
    }
}

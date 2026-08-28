package dev.rileyy.discordLink.providers;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserWhiteList;
import net.minecraft.server.players.UserWhiteListEntry;

import java.util.Optional;

public class WhitelistProvider {
    private final MinecraftServer server;
    private final UserWhiteList whitelist;

    public WhitelistProvider(MinecraftServer s) {
        this.server = s;
        this.whitelist = server.getPlayerList().getWhiteList();
    }

    public String[] getWhitelistedPlayers() { return this.whitelist.getUserList(); }

    public Boolean getWhitelistStatus() { return this.server.isUsingWhitelist(); }

    public boolean setWhitelistedPlayer(String username) {
        Optional<GameProfile> profile = server.services().profileResolver().fetchByName(username);
        if (profile.isEmpty()) return false;

        GameProfile gameProfile = profile.get();

        NameAndId nameAndId = new NameAndId(gameProfile);

        if (whitelist.isWhiteListed(nameAndId)) {
            return true;
        }

        UserWhiteListEntry entry = new UserWhiteListEntry(nameAndId);

        whitelist.add(entry);

        return true;
    }
    public boolean setWhitelistEnabled(boolean enabled) {
        server.setUsingWhitelist(enabled);
        if (enabled) {
            server.kickUnlistedPlayers();
        }
        return server.isUsingWhitelist();
    }

}

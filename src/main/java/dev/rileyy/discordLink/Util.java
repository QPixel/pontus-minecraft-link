package dev.rileyy.discordLink;

import dev.rileyy.discordLink.config.ConfigManager;
import dev.rileyy.discordLink.config.DiscordLinkConfig;
import io.grpc.Context;
import io.grpc.Metadata;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

public class Util {
    public static final String MOD_ID = "discord-link";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final DiscordLinkConfig CONFIG = ConfigManager.readAndGetConfig();

    public static final Metadata.Key<String> AUTHORIZATION_METADATA_KEY = Metadata.Key.of("Authorization", ASCII_STRING_MARSHALLER);
    public static final Context.Key<String> CLIENT_ID_CONTEXT_KEY = Context.key("clientId");

    public static SecretKey getKey(String encodedKey) {
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(encodedKey));
    }
}

package dev.rileyy.discordLink;

import dev.rileyy.discordLink.config.ConfigManager;
import dev.rileyy.discordLink.grpc.JwtCredential;
import dev.rileyy.discordLink.grpc.RPCServer;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.resources.Identifier;

import javax.crypto.SecretKey;
import java.io.IOException;

public class DiscordLink implements ModInitializer {
    public static final String MOD_ID = Util.MOD_ID;

    private RPCServer rpcServer;

    @Override
    public void onInitialize() {
        Util.LOGGER.info("Started");
        ConfigManager.readConfig();

        if (Util.CONFIG.getJWT_SIGNING_KEY().isEmpty()) {
            final SecretKey key = Jwts.SIG.HS256.key().build();
            final String encodedKey = Encoders.BASE64.encode(key.getEncoded());
            Util.CONFIG.setJWT_SIGNING_KEY(encodedKey);
        }
        final JwtCredential cred = new JwtCredential("default-client");
        Util.LOGGER.info("Testing JWT: {}", cred.makeJwt());

        rpcServer = new RPCServer(Util.CONFIG.getSERVER_URL(), Integer.parseInt(Util.CONFIG.getPORT()));

        registerRPCServer();
    }

    public void registerRPCServer() {
        ServerLifecycleEvents.SERVER_STARTED.register(s -> {
            try {
                rpcServer.start(s);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(s -> {
            rpcServer.stop();
        });
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}

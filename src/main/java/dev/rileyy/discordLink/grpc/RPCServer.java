package dev.rileyy.discordLink.grpc;


import dev.rileyy.discordLink.Util;
import io.grpc.Grpc;
import io.grpc.InsecureServerCredentials;
import io.grpc.Server;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.io.IOException;

public class RPCServer {
    private Server server;
    private int port;
    private String url;
    File certChain;
    File privateKey;

    public RPCServer(String url, int port, String certChainPath, String privateKeyPath) {
        this.port = port;
        this.url = url;

    }

    public void start(MinecraftServer s) throws IOException {
        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .addService(new MinecraftImpl(s))
                .intercept(new JwtServerInterceptor())  // add the JwtServerInterceptor
                .build()
                .start();
        Util.LOGGER.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                Util.LOGGER.error("*** shutting down gRPC server since JVM is shutting down");
                RPCServer.this.stop();
                Util.LOGGER.error("*** server shut down");
            }
        });
    }

    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }
}

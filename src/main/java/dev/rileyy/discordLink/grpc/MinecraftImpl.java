package dev.rileyy.discordLink.grpc;

import dev.rileyy.discordLink.providers.ServerStatusProvider;
import dev.rileyy.discordLink.providers.WhitelistProvider;
import io.grpc.stub.StreamObserver;
import minecraft.Minecraft;
import minecraft.MinecraftServerServiceGrpc;
import net.minecraft.server.MinecraftServer;

import java.util.Arrays;


public class MinecraftImpl extends MinecraftServerServiceGrpc.MinecraftServerServiceImplBase {
    private final WhitelistProvider whitelistProvider;
    private final ServerStatusProvider serverStatusProvider;

    public MinecraftImpl(MinecraftServer s) {
       whitelistProvider = new WhitelistProvider(s);
       serverStatusProvider = new ServerStatusProvider(s);
       super();
    }


    @Override
    public void getServerStatus(Minecraft.GetServerStatusRequest request, StreamObserver<Minecraft.GetServerStatusResponse> responseObserver) {
        Integer playerCount = serverStatusProvider.getPlayerCount();
        Integer maxPlayers = serverStatusProvider.getMaxPlayerCount();
        String motd = serverStatusProvider.getMOTD();
        String mcVersion = serverStatusProvider.getMCVersion();

        Minecraft.GetServerStatusResponse response = Minecraft.GetServerStatusResponse.newBuilder().setMaxPlayers(maxPlayers).setMotd(motd).setPlayers(playerCount).setVersion(mcVersion).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getPlayerList(Minecraft.GetPlayerListRequest request, StreamObserver<Minecraft.GetPlayerListResponse> responseObserver) {
        String[] playerList = serverStatusProvider.getPlayerList();
        Integer playerCount = serverStatusProvider.getPlayerCount();

        Minecraft.GetPlayerListResponse response = Minecraft.GetPlayerListResponse.newBuilder().addAllPlayers(Arrays.asList(playerList)).setTotalPlayers(playerCount).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void setWhitelist(Minecraft.SetWhitelistRequest request, StreamObserver<Minecraft.SetWhitelistResponse> responseObserver) {
        boolean success = false;
        if (request.hasEnabled()) {
          success = whitelistProvider.setWhitelistEnabled(request.getEnabled());
        } else if (request.hasPlayer()) {
            success = whitelistProvider.setWhitelistedPlayer(request.getPlayer());
        }

        Minecraft.SetWhitelistResponse response = Minecraft.SetWhitelistResponse.
                newBuilder().setSuccess(success).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}

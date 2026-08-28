package dev.rileyy.discordLink.grpc;

import dev.rileyy.discordLink.providers.WhitelistProvider;
import io.grpc.stub.StreamObserver;
import minecraft.Minecraft;
import minecraft.MinecraftServerServiceGrpc;
import net.minecraft.server.MinecraftServer;


public class MinecraftImpl extends MinecraftServerServiceGrpc.MinecraftServerServiceImplBase {
    private final WhitelistProvider whitelistProvider;
    public MinecraftImpl(MinecraftServer s) {
       whitelistProvider = new WhitelistProvider(s);

       super();
    }


    @Override
    public void getServerStatus(Minecraft.GetServerStatusRequest request, StreamObserver<Minecraft.GetServerStatusResponse> responseObserver) {
        super.getServerStatus(request, responseObserver);
    }

    @Override
    public void getPlayerList(Minecraft.GetPlayerListRequest request, StreamObserver<Minecraft.GetPlayerListResponse> responseObserver) {
        super.getPlayerList(request, responseObserver);
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

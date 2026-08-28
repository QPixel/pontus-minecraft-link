package dev.rileyy.discordLink.grpc;

import dev.rileyy.discordLink.Util;
import io.grpc.CallCredentials;
import io.grpc.Metadata;
import io.grpc.Status;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.concurrent.Executor;

public class JwtCredential extends CallCredentials {
    private final String subject;

    public JwtCredential(String subject) {
        this.subject = subject;
    }
    public String makeJwt() {
        return Jwts.builder().subject(subject).signWith(Util.getKey(Util.CONFIG.getJWT_SIGNING_KEY()))
                        .compact();
    }

    @Override
    public void applyRequestMetadata(final RequestInfo requestInfo, final Executor executor,
                                    final MetadataApplier metadataApplier) {

        final String jwt = makeJwt();

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Metadata headers = new Metadata();
                    headers.put(Util.AUTHORIZATION_METADATA_KEY,
                            String.format("%s %s", Util.CONFIG.getBEARER_TYPE(), jwt));
                    metadataApplier.apply(headers);

                } catch (Throwable e) {
                    metadataApplier.fail(Status.UNAUTHENTICATED.withCause(e));
                }
            }
        });

    }
}
package dev.rileyy.discordLink.grpc;

import dev.rileyy.discordLink.Util;
import io.grpc.*;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class JwtServerInterceptor implements ServerInterceptor {

    private final JwtParser parser = Jwts.parser()
            .verifyWith(Util.getKey(Util.CONFIG.getJWT_SIGNING_KEY()))
            .build();

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall,
                                                                 Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String value = metadata.get(Util.AUTHORIZATION_METADATA_KEY);


        Status status = Status.OK;
        if (value == null) {
            status = Status.UNAUTHENTICATED.withDescription("Authorization token is missing");
        } else if (!value.startsWith(Util.CONFIG.getBEARER_TYPE())) {
            status = Status.UNAUTHENTICATED.withDescription("Unknown authorization type");
        } else {
            Jws<Claims> claims = null;
            // remove authorization type prefix
            String token = value.substring(Util.CONFIG.getBEARER_TYPE().length()).trim();
            try {
                // verify token signature and parse claims
                claims = parser.parseSignedClaims(token);
            } catch (JwtException e) {
                status = Status.UNAUTHENTICATED.withDescription(e.getMessage()).withCause(e);
            }
            if (claims != null) {
                // set client id into current context
                Context ctx = Context.current()
                        .withValue(Util.CLIENT_ID_CONTEXT_KEY, claims.getBody().getSubject());
                return Contexts.interceptCall(ctx, serverCall, metadata, serverCallHandler);
            }
        }
        Util.LOGGER.info("Error status {}", status);
        serverCall.close(status, new Metadata());
        return new ServerCall.Listener<ReqT>() {
            // noop
        };
    }
}

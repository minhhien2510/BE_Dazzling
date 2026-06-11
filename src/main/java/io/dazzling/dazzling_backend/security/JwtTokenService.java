package io.dazzling.dazzling_backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.dazzling.dazzling_backend.dto.TokenInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class JwtTokenService {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtTokenService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long accessExpirationMs,
            @Value("${jwt.refresh-expiration-ms}") long refreshExpirationMs) {
        this.algorithm = Algorithm.HMAC256(secret.getBytes());
        this.verifier = JWT.require(algorithm).build();
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public String generateAccessToken(String email, String role) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(email)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusMillis(accessExpirationMs)))
                .withClaim("email", email)
                .withClaim("role", role)
                .withClaim("tokenType", "ACCESS")
                .sign(algorithm);
    }

    public String generateRefreshToken(String email, String role) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(email)
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(now.plusMillis(refreshExpirationMs)))
                .withClaim("email", email)
                .withClaim("role", role)
                .withClaim("tokenType", "REFRESH")
                .sign(algorithm);
    }

    public String getEmailFromToken(String token) {
        return decodeToken(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return decodeToken(token).getClaim("role").asString();
    }

    public TokenInfoResponse parseTokenInfo(String token) {
        DecodedJWT decoded = decodeToken(token);
        return new TokenInfoResponse(
                decoded.getSubject(),
                decoded.getClaim("email").asString(),
                decoded.getClaim("role").asString(),
                decoded.getClaim("tokenType").asString(),
                decoded.getIssuedAt().getTime(),
                decoded.getExpiresAt().getTime()
        );
    }

    public boolean validateAccessToken(String token) {
        try {
            DecodedJWT decoded = decodeToken(token);
            return "ACCESS".equals(decoded.getClaim("tokenType").asString());
        } catch (JWTVerificationException ex) {
            return false;
        }
    }

    public boolean validateRefreshToken(String token) {
        try {
            DecodedJWT decoded = decodeToken(token);
            return "REFRESH".equals(decoded.getClaim("tokenType").asString());
        } catch (JWTVerificationException ex) {
            return false;
        }
    }

    public long getRefreshExpirationMs() {
        return refreshExpirationMs;
    }

    private DecodedJWT decodeToken(String token) {
        return verifier.verify(token);
    }
}

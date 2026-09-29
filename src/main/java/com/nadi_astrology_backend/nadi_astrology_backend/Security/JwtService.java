package com.nadi_astrology_backend.nadi_astrology_backend.Security;

import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration
    ) {

        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException(
                    "JWT secret must not be empty"
            );
        }

        if (secret.length() < 32) {
            throw new IllegalArgumentException(
                    "JWT secret must contain at least 32 characters"
            );
        }

        this.secretKey =
                Keys.hmacShaKeyFor(
                        secret.getBytes(StandardCharsets.UTF_8)
                );

        if (accessTokenExpiration <= 0) {
            throw new IllegalArgumentException(
                    "JWT access token expiration must be greater than 0"
            );
        }

        this.accessTokenExpiration =
                accessTokenExpiration;
    }

    // ============================================================
    // GENERATE ACCESS TOKEN
    // ============================================================

    public String generateAccessToken(User user) {

        if (user == null) {
            throw new IllegalArgumentException(
                    "User cannot be null"
            );
        }

        if (user.getUserId() == null) {
            throw new IllegalArgumentException(
                    "User ID cannot be null"
            );
        }

        if (user.getEmail() == null ||
                user.getEmail().isBlank()) {

            throw new IllegalArgumentException(
                    "User email cannot be null or empty"
            );
        }

        Date now = new Date();

        Date expiration =
                new Date(
                        now.getTime()
                                + accessTokenExpiration
                );

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getUserId())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    // ============================================================
    // EXTRACT & VALIDATE CLAIMS
    // ============================================================

    public Claims extractClaims(String token) {

        if (token == null ||
                token.isBlank()) {

            throw new IllegalArgumentException(
                    "JWT token cannot be empty"
            );
        }

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ============================================================
    // VALIDATE TOKEN
    // ============================================================

    public boolean isTokenValid(String token) {

        try {

            Claims claims =
                    extractClaims(token);

            return claims.getSubject() != null
                    && !claims.getSubject().isBlank()
                    && claims.get("userId") != null;

        } catch (Exception exception) {

            return false;
        }
    }
}
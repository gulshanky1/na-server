package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.UnauthorizedException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.RefreshToken;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    private final SecureRandom secureRandom =
            new SecureRandom();

    @Transactional
    public String createRefreshToken(
            User user,
            String tokenFamily
    ) {

        byte[] randomBytes =
                new byte[64];

        secureRandom.nextBytes(
                randomBytes
        );

        String rawToken =
                Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(
                                randomBytes
                        );

        String tokenHash =
                hashToken(rawToken);

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .user(user)
                        .tokenHash(tokenHash)
                        .tokenFamily(tokenFamily)
                        .expiresAt(
                                LocalDateTime.now()
                                        .plusNanos(
                                                refreshTokenExpiration
                                                        * 1_000_000
                                        )
                        )
                        .build();

        refreshTokenRepository.save(
                refreshToken
        );

        return rawToken;
    }

    @Transactional
    public RefreshTokenValidationResult validateAndRevoke(
            String rawToken
    ) {

        if (rawToken == null ||
                rawToken.isBlank()) {

            throw new UnauthorizedException(
                    "Refresh token is required"
            );
        }

        String tokenHash =
                hashToken(rawToken);

        /*
         * PESSIMISTIC_WRITE lock.
         *
         * This prevents two concurrent requests
         * from rotating the same refresh token.
         */
        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByTokenHashForUpdate(
                                tokenHash
                        )
                        .orElseThrow(() ->
                                new UnauthorizedException(
                                        "Invalid refresh token"
                                )
                        );

        /*
         * Reuse detection.
         *
         * If the token was already revoked,
         * revoke the complete token family.
         */
        if (refreshToken.getRevokedAt() != null) {

            revokeTokenFamily(
                    refreshToken.getTokenFamily()
            );

            throw new UnauthorizedException(
                    "Refresh token reuse detected"
            );
        }

        /*
         * Check expiration.
         */
        if (refreshToken.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            throw new UnauthorizedException(
                    "Refresh token has expired"
            );
        }

        User user =
                refreshToken.getUser();

        /*
         * Check account status.
         */
        if (!user.isAccountEnabled()) {

            throw new UnauthorizedException(
                    "Account is disabled"
            );
        }

        /*
         * Revoke current token.
         */
        refreshToken.setRevokedAt(
                LocalDateTime.now()
        );

        refreshTokenRepository.save(
                refreshToken
        );

        return new RefreshTokenValidationResult(
                user,
                refreshToken.getTokenFamily()
        );
    }

    @Transactional
    public void revokeToken(
            String rawToken
    ) {

        if (rawToken == null ||
                rawToken.isBlank()) {

            throw new UnauthorizedException(
                    "Refresh token is required"
            );
        }

        String tokenHash =
                hashToken(rawToken);

        RefreshToken refreshToken =
                refreshTokenRepository
                        .findByTokenHashForUpdate(
                                tokenHash
                        )
                        .orElseThrow(() ->
                                new UnauthorizedException(
                                        "Invalid refresh token"
                                )
                        );

        if (refreshToken.getRevokedAt() == null) {

            refreshToken.setRevokedAt(
                    LocalDateTime.now()
            );

            refreshTokenRepository.save(
                    refreshToken
            );
        }
    }

    @Transactional
    public void revokeTokenFamily(
            String tokenFamily
    ) {

        List<RefreshToken> tokens =
                refreshTokenRepository
                        .findByTokenFamily(
                                tokenFamily
                        );

        LocalDateTime now =
                LocalDateTime.now();

        for (RefreshToken token : tokens) {

            if (token.getRevokedAt() == null) {

                token.setRevokedAt(now);
            }
        }

        refreshTokenRepository.saveAll(
                tokens
        );
    }

    private String hashToken(
            String token
    ) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance(
                            "SHA-256"
                    );

            byte[] hash =
                    digest.digest(
                            token.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );

            StringBuilder result =
                    new StringBuilder();

            for (byte b : hash) {

                result.append(
                        String.format(
                                "%02x",
                                b
                        )
                );
            }

            return result.toString();

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Unable to hash refresh token",
                    e
            );
        }
    }
}
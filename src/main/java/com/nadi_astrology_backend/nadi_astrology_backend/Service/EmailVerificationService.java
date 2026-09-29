package com.nadi_astrology_backend.nadi_astrology_backend.Service;

import com.nadi_astrology_backend.nadi_astrology_backend.Enum.AuthProvider;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.BadRequestException;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.RateLimitException;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.EmailVerificationToken;
import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.EmailVerificationTokenRepository;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final RateLimitService rateLimitService;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    private final SecureRandom secureRandom = new SecureRandom();
    private final UserRepository userRepository;
    private static final int TOKEN_EXPIRY_HOURS = 24;

    @Transactional
    public void createAndSendVerificationToken(User user) {

        tokenRepository.deleteByUser(user);

        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);

        String rawToken =
                Base64.getUrlEncoder()
                        .withoutPadding()
                        .encodeToString(randomBytes);

        String tokenHash = hashToken(rawToken);

        EmailVerificationToken verificationToken =
                EmailVerificationToken.builder()
                        .user(user)
                        .tokenHash(tokenHash)
                        .expiresAt(
                                LocalDateTime.now()
                                        .plusHours(TOKEN_EXPIRY_HOURS)
                        )
                        .build();

        tokenRepository.save(verificationToken);

        String verificationUrl =
                frontendUrl
                        + "/verify-email?token="
                        + rawToken;

        String html = buildVerificationEmail(
                user.getFullName(),
                verificationUrl
        );

        emailService.sendHtmlEmail(
                user.getEmail(),
                "Verify your Nadi Astrology account",
                html
        );
    }

    @Transactional
    public void verifyEmail(String rawToken) {

        if (rawToken == null || rawToken.isBlank()) {
            throw new BadRequestException(
                    "Verification token is required"
            );
        }

        String tokenHash = hashToken(rawToken);

        EmailVerificationToken verificationToken =
                tokenRepository.findByTokenHash(tokenHash)
                        .orElseThrow(() ->
                                new BadRequestException(
                                        "Invalid verification token"
                                )
                        );

        if (verificationToken.getUsedAt() != null) {
            throw new BadRequestException(
                    "Verification token has already been used"
            );
        }

        if (verificationToken.getExpiresAt()
                .isBefore(LocalDateTime.now())) {

            throw new BadRequestException(
                    "Verification token has expired"
            );
        }

        User user =
                verificationToken.getUser();

        user.setEmailVerified(true);

        verificationToken.setUsedAt(
                LocalDateTime.now()
        );

        tokenRepository.save(verificationToken);
    }

    private String hashToken(String token) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

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
                        String.format("%02x", b)
                );
            }

            return result.toString();

        } catch (NoSuchAlgorithmException e) {

            throw new IllegalStateException(
                    "Unable to hash verification token",
                    e
            );
        }
    }

    private String buildVerificationEmail(
            String fullName,
            String verificationUrl
    ) {

        return """
                <!DOCTYPE html>
                <html>
                <body style="font-family: Arial, sans-serif; background:#f7f7f7; padding:30px;">
                
                    <div style="max-width:600px; margin:auto; background:white; padding:30px; border-radius:10px;">
                
                        <h2 style="color:#8b5e3c;">
                            Welcome to Nadi Astrology
                        </h2>
                
                        <p>Hello %s,</p>
                
                        <p>
                            Your account has been created successfully.
                            Please verify your email address to activate your account.
                        </p>
                
                        <p style="text-align:center; margin:30px 0;">
                            <a href="%s"
                               style="background:#8b5e3c; color:white; padding:12px 24px; text-decoration:none; border-radius:6px;">
                                Verify Email
                            </a>
                        </p>
                
                        <p>
                            This verification link will expire in 24 hours.
                        </p>
                
                        <p>
                            If you did not create this account, you can safely ignore this email.
                        </p>
                
                        <p>
                            Regards,<br>
                            Nadi Astrology
                        </p>
                
                    </div>
                
                </body>
                </html>
                """.formatted(
                fullName,
                verificationUrl
        );
    }

    @Transactional
    public void resendVerificationEmail(
            String email,
            String clientIp
    ) {

        String normalizedEmail = email.trim().toLowerCase();

        boolean emailAllowed = rateLimitService.allowed(
                "rate:email-verification:" + normalizedEmail,
                3,
                3600
        );

        if (!emailAllowed) {
            throw new RateLimitException(
                    "Too many verification requests. Please try again later."
            );
        }

        boolean ipAllowed = rateLimitService.allowed(
                "rate:email-verification-ip:" + clientIp,
                10,
                3600
        );

        if (!ipAllowed) {
            throw new RateLimitException(
                    "Too many verification requests. Please try again later."
            );
        }

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new BadRequestException(
                                "Unable to process verification request"
                        ));

        if (user.isEmailVerified()) {
            throw new BadRequestException(
                    "Email is already verified"
            );
        }

        if (user.getAuthProvider() != AuthProvider.LOCAL) {
            throw new BadRequestException(
                    "Email verification is not required for this account"
            );
        }

        createAndSendVerificationToken(user);
    }
}
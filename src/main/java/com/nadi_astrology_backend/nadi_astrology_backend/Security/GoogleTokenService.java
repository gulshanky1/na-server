package com.nadi_astrology_backend.nadi_astrology_backend.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.UnauthorizedException;

@Service
@RequiredArgsConstructor
public class GoogleTokenService {

    private final JwtDecoder jwtDecoder;

    @Value("${google.client-id}")
    private String googleClientId;

    public GoogleUserInfo verify(String idToken) {

        try {

            Jwt jwt = jwtDecoder.decode(idToken);

            String issuer = jwt.getIssuer() != null
                    ? jwt.getIssuer().toString()
                    : null;

            if (!"https://accounts.google.com".equals(issuer)
                    && !"accounts.google.com".equals(issuer)) {

                throw new UnauthorizedException(
                        "Invalid Google token issuer"
                );
            }

            String audience = jwt.getAudience()
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (!googleClientId.equals(audience)) {
                throw new UnauthorizedException(
                        "Invalid Google token audience"
                );
            }

            String email = jwt.getClaimAsString("email");
            String subject = jwt.getSubject();
            String name = jwt.getClaimAsString("name");
            String picture = jwt.getClaimAsString("picture");

            Boolean emailVerified =
                    jwt.getClaimAsBoolean("email_verified");

            if (email == null || email.isBlank()) {
                throw new UnauthorizedException(
                        "Google email not found"
                );
            }

            if (subject == null || subject.isBlank()) {
                throw new UnauthorizedException(
                        "Google user ID not found"
                );
            }

            if (!Boolean.TRUE.equals(emailVerified)) {
                throw new UnauthorizedException(
                        "Google email is not verified"
                );
            }

            return new GoogleUserInfo(
                    email.trim().toLowerCase(),
                    subject,
                    name,
                    picture
            );

        } catch (JwtException exception) {

            throw new UnauthorizedException(
                    "Invalid Google authentication token"
            );
        }
    }
}
package com.nadi_astrology_backend.nadi_astrology_backend.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.JwtClaimValidator;

@Configuration
public class GoogleSecurityConfig {

    @Value("${google.client-id}")
    private String googleClientId;

    @Bean
    public JwtDecoder googleJwtDecoder() {

        JwtDecoder decoder =
                JwtDecoders.fromIssuerLocation(
                        "https://accounts.google.com"
                );

        OAuth2TokenValidator<Jwt> issuerValidator =
                JwtValidators.createDefaultWithIssuer(
                        "https://accounts.google.com"
                );

        OAuth2TokenValidator<Jwt> audienceValidator =
                new JwtClaimValidator<>(
                        "aud",
                        audience -> audience != null
                                && audience.toString()
                                .contains(googleClientId)
                );

        OAuth2TokenValidator<Jwt> validator =
                new DelegatingOAuth2TokenValidator<>(
                        issuerValidator,
                        audienceValidator
                );

        ((org.springframework.security.oauth2.jwt.NimbusJwtDecoder) decoder)
                .setJwtValidator(validator);

        return decoder;
    }
}
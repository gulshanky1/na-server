package com.nadi_astrology_backend.nadi_astrology_backend.Config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Nadi Astrology Backend API",
                version = "1.0",
                description = """
                        REST API for Nadi Astrology platform.

                        This API provides authentication, user management,
                        courses, books, services, phone consultations,
                        products, orders, payments, student enrollment,
                        live classes and administration features.
                        """,
                contact = @Contact(
                        name = "Nadi Astrology",
                        email = "info@nadi-astrology.com",
                        url = "https://nadi-astrology.com"
                )
        ),
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        description = "Enter your JWT access token"
)
public class OpenApiConfig {
}
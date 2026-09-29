package com.nadi_astrology_backend.nadi_astrology_backend.Config;

import com.nadi_astrology_backend.nadi_astrology_backend.Security.JwtAccessDeniedHandler;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.JwtAuthenticationEntryPoint;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                // ==========================================
                // CSRF
                // ==========================================
                .csrf(csrf -> csrf.disable())

                // ==========================================
                // CORS
                // ==========================================
                .cors(Customizer.withDefaults())

                // ==========================================
                // Stateless JWT Authentication
                // ==========================================
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // ==========================================
                // HTTP Security Headers
                // ==========================================
                .headers(headers -> headers

                        // Prevent MIME-type sniffing
                        .contentTypeOptions(
                                Customizer.withDefaults()
                        )

                        // Prevent clickjacking
                        .frameOptions(frame ->
                                frame.deny()
                        )

                        // Control Referer information
                        .referrerPolicy(referrer ->
                                referrer.policy(
                                        org.springframework.security.web.header.writers
                                                .ReferrerPolicyHeaderWriter
                                                .ReferrerPolicy
                                                .STRICT_ORIGIN_WHEN_CROSS_ORIGIN
                                )
                        )

                        // HSTS
                        // Browser will enforce HTTPS after receiving this
                        .httpStrictTransportSecurity(hsts ->
                                hsts
                                        .includeSubDomains(true)
                                        .preload(false)
                                        .maxAgeInSeconds(31536000)
                        )

                        // Additional browser restrictions
                        .addHeaderWriter(
                                new StaticHeadersWriter(
                                        "Permissions-Policy",
                                        "camera=(), microphone=(), geolocation=(), payment=()"
                                )
                        )
                )

                // ==========================================
                // Authorization
                // ==========================================
                .authorizeHttpRequests(auth -> auth

                        // CORS preflight
                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        ).permitAll()

                        // ==========================================
                        // Authentication
                        // ==========================================
                        .requestMatchers(
                                "/api/v1/users/register",
                                "/api/v1/auth/login",
                                "/api/v1/auth/google",
                                "/api/v1/auth/verify-email",
                                "/api/v1/auth/resend-verification",
                                "/api/v1/auth/refresh",
                                "/api/v1/auth/logout"
                        ).permitAll()

                        // ==========================================
                        // Public Consultations
                        // ==========================================
                        .requestMatchers(
                                "/api/v1/consultations",
                                "/api/v1/consultations/**"
                        ).permitAll()

                        // ==========================================
                        // Public Products
                        // ==========================================
                        .requestMatchers(
                                "/api/v1/products",
                                "/api/v1/products/**"
                        ).permitAll()

                        // ==========================================
                        // Public Courses
                        // ==========================================
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/courses",
                                "/api/v1/courses/**"
                        ).permitAll()

                        // ==========================================
                        // Public Books
                        // ==========================================
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/books",
                                "/api/v1/books/**"
                        ).permitAll()

                        // ==========================================
                        // Public Services
                        // ==========================================
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/services",
                                "/api/v1/services/**"
                        ).permitAll()

                        // ==========================================
                        // Public Blogs
                        // ==========================================
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/blogs",
                                "/api/v1/blogs/**"
                        ).permitAll()

                        // ==========================================
                        // Swagger - Development
                        // ==========================================
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // ==========================================
                        // Test Email - Development
                        // ==========================================
                        .requestMatchers(
                                "/api/v1/test/email"
                        ).permitAll()

                        // ==========================================
                        // Authenticated User Endpoints
                        // ==========================================
                        .requestMatchers(
                                "/api/v1/users/me",
                                "/api/v1/profile/**",
                                "/api/v1/students/**",
                                "/api/v1/enrollments/**",
                                "/api/v1/checkout/**",
                                "/api/v1/payments/**",
                                "/api/v1/live-classes/me",
                                "/api/v1/notifications/**",
                                "/api/v1/service-fulfillments/**"
                        ).hasAnyRole(
                                "USER",
                                "ADMIN"
                        )

                        // ==========================================
                        // Admin Endpoints
                        // ==========================================
                        .requestMatchers(
                                "/api/v1/admin/**"
                        ).hasRole("ADMIN")

                        // ==========================================
                        // Everything Else
                        // ==========================================
                        .anyRequest().authenticated()
                )

                // ==========================================
                // Exception Handling
                // ==========================================
                .exceptionHandling(exception -> exception

                        .authenticationEntryPoint(
                                jwtAuthenticationEntryPoint
                        )

                        .accessDeniedHandler(
                                jwtAccessDeniedHandler
                        )
                )

                // ==========================================
                // JWT Filter
                // ==========================================
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
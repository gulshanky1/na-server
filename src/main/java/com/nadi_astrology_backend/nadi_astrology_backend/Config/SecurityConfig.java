package com.nadi_astrology_backend.nadi_astrology_backend.Config;

import com.nadi_astrology_backend.nadi_astrology_backend.Security.JwtAccessDeniedHandler;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.JwtAuthenticationEntryPoint;
import com.nadi_astrology_backend.nadi_astrology_backend.Security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
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

                // =====================================================
                // CSRF
                // =====================================================

                .csrf(csrf -> csrf.disable())


                // =====================================================
                // CORS
                // =====================================================

                .cors(Customizer.withDefaults())


                // =====================================================
                // AUTHORIZATION
                // =====================================================

                .authorizeHttpRequests(auth -> auth


                        // =================================================
                        // OPTIONS
                        // =================================================

                        .requestMatchers(
                                HttpMethod.OPTIONS,
                                "/**"
                        )
                        .permitAll()


                        // =================================================
                        // PUBLIC - AUTH
                        // =================================================

                        .requestMatchers(
                                "/api/v1/users/register",
                                "/api/v1/auth/login"
                        )
                        .permitAll()


                        // =================================================
                        // PUBLIC - CONSULTATIONS
                        // =================================================

                        .requestMatchers(
                                "/api/v1/consultations",
                                    "/api/v1/consultations/**"
                        )
                        .permitAll()


                        // =================================================
                        // PUBLIC - PRODUCTS
                        // =================================================

                        .requestMatchers(
                                "/api/v1/products",
                                "/api/v1/products/**"
                        )
                        .permitAll()


                        // =================================================
                        // PUBLIC - COURSES
                        // =================================================

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/courses/"
                        )
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/v1/courses/*"
                        )
                        .permitAll()


                        // =================================================
                        // SWAGGER
                        // =================================================

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api/v1/test/email",
                                "/api/v1/auth/google",
                                "/api/v1/courses/",
                                "/api/v1/courses/**",
                                "/api/v1/books",
                                "/api/v1/books/**",
                                "/api/v1/services",
                                "/api/v1/services/**",
                                "/api/v1/blogs",
                                "/api/v1/blogs/**"

                        )
                        .permitAll()


                        // =================================================
                        // USER + ADMIN
                        // =================================================

                        .requestMatchers(
                                "/api/v1/users/me",
                                "/api/v1/profile/**",
                                "/api/v1/students/**",
                                "/api/v1/enrollments/**",
                                "/api/v1/checkout/**",
                                "/api/v1/books/**",
                                "/api/v1/payments/**",
                                "/api/v1/live-classes/me",
                                "/api/v1/notifications/**",
                                "/api/v1/service-fulfillments/**"
                        )
                        .hasAnyRole(
                                "USER",
                                "ADMIN"
                        )


                        // =================================================
                        // ADMIN
                        // =================================================

                        .requestMatchers(
                                "/api/v1/admin/**"
                        )
                        .hasRole("ADMIN")


                        // =================================================
                        // EVERYTHING ELSE
                        // =================================================

                        .anyRequest()
                        .authenticated()
                )


                // =====================================================
                // EXCEPTION HANDLING
                // =====================================================

                .exceptionHandling(exception -> exception

                        // 401
                        .authenticationEntryPoint(
                                jwtAuthenticationEntryPoint
                        )

                        // 403
                        .accessDeniedHandler(
                                jwtAccessDeniedHandler
                        )
                )


                // =====================================================
                // JWT FILTER
                // =====================================================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();
    }


    // =====================================================
    // PASSWORD ENCODER
    // =====================================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
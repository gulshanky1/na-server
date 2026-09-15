package com.nadi_astrology_backend.nadi_astrology_backend.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        log.info("JWT FILTER -> {} {}", request.getMethod(), request.getRequestURI());
        String authHeader = request.getHeader("Authorization");

        // No Authorization header or wrong format
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // Remove "Bearer " from the header
        String token = authHeader.substring(7);

        try {

            // Extract and verify JWT claims
            var claims = jwtService.extractClaims(token);

            String email = claims.getSubject();
            Long userId = claims.get("userId", Long.class);
            String role = claims.get("role", String.class);

            log.info(
                    "JWT claims -> email: {}, userId: {}, role: {}",
                    email,
                    userId,
                    role
            );

            // Make sure required claims exist
            if (email != null &&
                    userId != null &&
                    role != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {

                // Spring Security expects ROLE_ prefix
                var authority =
                        new SimpleGrantedAuthority(
                                "ROLE_" + role
                        );

                AuthenticatedUser authenticatedUser =
                        new AuthenticatedUser(
                                userId,
                                email,
                                role,
                                List.of(authority)
                        );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                authenticatedUser,
                                null,
                                authenticatedUser.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // Store authenticated user in SecurityContext
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

                log.info(
                        "JWT authentication successful -> email: {}, userId: {}, role: {}",
                        email,
                        userId,
                        role
                );
            }

        } catch (Exception e) {

            // JWT is invalid, expired, malformed, or signature is invalid
            log.error(
                    "JWT authentication failed: {}",
                    e.getMessage()
            );
        }

        // Continue request processing
        log.info("JWT FILTER -> {} {}", request.getMethod(), request.getRequestURI());
        filterChain.doFilter(request, response);


    }
}
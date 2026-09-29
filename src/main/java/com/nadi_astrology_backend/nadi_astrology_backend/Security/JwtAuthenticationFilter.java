package com.nadi_astrology_backend.nadi_astrology_backend.Security;

import com.nadi_astrology_backend.nadi_astrology_backend.Models.User;
import com.nadi_astrology_backend.nadi_astrology_backend.Repositories.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader =
                request.getHeader("Authorization");

        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token =
                authHeader.substring(7).trim();

        if (token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            Claims claims =
                    jwtService.extractClaims(token);

            String email =
                    claims.getSubject();

            Long userId =
                    claims.get("userId", Long.class);

            if (email == null ||
                    email.isBlank() ||
                    userId == null) {

                filterChain.doFilter(request, response);
                return;
            }

            if (SecurityContextHolder
                    .getContext()
                    .getAuthentication() == null) {

                Optional<User> optionalUser =
                        userRepository.findById(userId);

                if (optionalUser.isEmpty()) {

                    filterChain.doFilter(request, response);
                    return;
                }

                User user = optionalUser.get();

                if (!user.isAccountEnabled()) {

                    filterChain.doFilter(request, response);
                    return;
                }

                if (!user.getEmail().equalsIgnoreCase(email)) {

                    filterChain.doFilter(request, response);
                    return;
                }

                String role =
                        user.getRole().name();

                SimpleGrantedAuthority authority =
                        new SimpleGrantedAuthority(
                                "ROLE_" + role
                        );

                AuthenticatedUser authenticatedUser =
                        new AuthenticatedUser(
                                user.getUserId(),
                                user.getEmail(),
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

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);
            }

        } catch (Exception ignored) {
            // Invalid/expired JWT.
            // Leave SecurityContext unauthenticated.
        }

        filterChain.doFilter(request, response);
    }
}
package com.nadi_astrology_backend.nadi_astrology_backend.Security;


import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ApiErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint
        implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        ApiErrorResponse errorResponse =
                ApiErrorResponse.builder()
                        .error(false)
                        .message("Authentication required")
                        .status(HttpServletResponse.SC_UNAUTHORIZED)
                        .timestamp(LocalDateTime.now())
                        .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        response.getWriter().write(
                objectMapper.writeValueAsString(errorResponse)
        );
    }
}
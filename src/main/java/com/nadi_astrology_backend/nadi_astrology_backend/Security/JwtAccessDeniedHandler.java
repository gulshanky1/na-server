package com.nadi_astrology_backend.nadi_astrology_backend.Security;


import com.nadi_astrology_backend.nadi_astrology_backend.Exceptions.ApiErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        ApiErrorResponse errorResponse =
                ApiErrorResponse.builder()
                        .success(false)
                        .message("Access denied")
                        .status(HttpServletResponse.SC_FORBIDDEN)
                        .timestamp(LocalDateTime.now())
                        .build();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        response.getWriter().write(
                objectMapper.writeValueAsString(errorResponse)
        );
    }
}
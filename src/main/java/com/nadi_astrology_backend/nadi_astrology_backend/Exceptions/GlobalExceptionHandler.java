package com.nadi_astrology_backend.nadi_astrology_backend.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ==========================================
    // Duplicate Resource - 409
    // ==========================================

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateResource(
            DuplicateResourceException exception) {

        return buildResponse(
                exception.getMessage(),
                HttpStatus.CONFLICT
        );
    }
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(
            BadRequestException exception) {

        return buildResponse(
                exception.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }


    // ==========================================
    // Resource Not Found - 404
    // ==========================================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
            ResourceNotFoundException exception) {

        return buildResponse(
                exception.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }


    // ==========================================
    // Unauthorized - 401
    // ==========================================

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(
            UnauthorizedException exception) {

        return buildResponse(
                exception.getMessage(),
                HttpStatus.UNAUTHORIZED
        );
    }


    // ==========================================
    // Validation Error - 400
    // ==========================================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception) {

        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Validation failed");

        return buildResponse(
                message,
                HttpStatus.BAD_REQUEST
        );
    }


    // ==========================================
    // Common Response Builder
    // ==========================================

    private ResponseEntity<ApiErrorResponse> buildResponse(
            String message,
            HttpStatus status) {

        ApiErrorResponse response = ApiErrorResponse.builder()
                .success(false)
                .message(message)
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(status)
                .body(response);
    }
}
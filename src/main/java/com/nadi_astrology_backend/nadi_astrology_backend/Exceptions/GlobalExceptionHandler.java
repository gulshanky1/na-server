package com.nadi_astrology_backend.nadi_astrology_backend.Exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateResource(
            DuplicateResourceException exception) {

        return buildResponse(
                exception.getMessage(),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException exception) {

        return buildResponse(
                "The requested operation conflicts with existing data",
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

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFound(
            ResourceNotFoundException exception) {

        return buildResponse(
                exception.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(
            UnauthorizedException exception) {

        return buildResponse(
                exception.getMessage(),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiErrorResponse> handleForbidden(
            ForbiddenException exception) {

        return buildResponse(
                exception.getMessage(),
                HttpStatus.FORBIDDEN
        );
    }

    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ApiErrorResponse> handleRateLimitException(
            RateLimitException exception) {

        return buildResponse(
                exception.getMessage(),
                HttpStatus.TOO_MANY_REQUESTS
        );
    }

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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolation(
            ConstraintViolationException exception) {

        String message = exception.getConstraintViolations()
                .stream()
                .findFirst()
                .map(ConstraintViolation::getMessage)
                .orElse("Validation failed");

        return buildResponse(
                message,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(
            org.springframework.http.converter.HttpMessageNotReadableException.class
    )
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(
            org.springframework.http.converter.HttpMessageNotReadableException exception) {

        return buildResponse(
                "Invalid request body",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleTypeMismatch(
            MethodArgumentTypeMismatchException exception) {

        return buildResponse(
                "Invalid request parameter",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingParameter(
            MissingServletRequestParameterException exception) {

        return buildResponse(
                "Required request parameter is missing",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException exception) {

        return buildResponse(
                "HTTP method is not supported for this endpoint",
                HttpStatus.METHOD_NOT_ALLOWED
        );
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException exception) {

        return buildResponse(
                "Unsupported media type",
                HttpStatus.UNSUPPORTED_MEDIA_TYPE
        );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiErrorResponse> handleMaxUploadSizeExceeded(
            MaxUploadSizeExceededException exception) {

        return buildResponse(
                "Uploaded file is too large",
                HttpStatus.PAYLOAD_TOO_LARGE
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(
            Exception exception) {

        return buildResponse(
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(
            String message,
            HttpStatus status) {

        ApiErrorResponse response =
                ApiErrorResponse.builder()
                        .error(true)
                        .message(message)
                        .status(status.value())
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity
                .status(status)
                .body(response);
    }
}
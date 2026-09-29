package com.nadi_astrology_backend.nadi_astrology_backend.Exceptions;

public class RateLimitException extends RuntimeException {

    public RateLimitException(String message) {
        super(message);
    }
}
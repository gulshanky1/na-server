package com.nadi_astrology_backend.nadi_astrology_backend.Exceptions;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
package com.nadi_astrology_backend.nadi_astrology_backend.Exceptions;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
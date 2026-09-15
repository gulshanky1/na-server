package com.nadi_astrology_backend.nadi_astrology_backend.Exceptions;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
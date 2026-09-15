package com.nadi_astrology_backend.nadi_astrology_backend.Exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
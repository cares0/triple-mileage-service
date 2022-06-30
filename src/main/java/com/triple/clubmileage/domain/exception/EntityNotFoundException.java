package com.triple.clubmileage.domain.exception;

public class EntityNotFoundException extends IllegalArgumentException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}

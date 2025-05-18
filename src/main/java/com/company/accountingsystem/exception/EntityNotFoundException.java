package com.company.accountingsystem.exception;

// This exception is used when a specific entity (such as a product, user, or order) cannot be found in the database.
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
package com.company.accountingsystem.exception;

// This exception is thrown when the client sends a request that is syntactically invalid or cannot be processed due to some client-side error.
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

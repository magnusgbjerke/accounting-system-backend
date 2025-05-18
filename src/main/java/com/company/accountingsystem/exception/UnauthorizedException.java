package com.company.accountingsystem.exception;

// Used when authentication is required, but the user has not provided valid credentials.
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
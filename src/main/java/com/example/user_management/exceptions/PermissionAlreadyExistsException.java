package com.example.user_management.exceptions;

public class PermissionAlreadyExistsException extends RuntimeException {

    public PermissionAlreadyExistsException(String message) {
        super(message);
    }
}

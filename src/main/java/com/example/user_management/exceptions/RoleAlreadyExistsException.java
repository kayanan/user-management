package com.example.user_management.exceptions;

public class RoleAlreadyExistsException extends RuntimeException {

   public RoleAlreadyExistsException(String message) {
        super(message);
    }
}

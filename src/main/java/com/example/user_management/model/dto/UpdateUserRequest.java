package com.example.user_management.model.dto;

public record UpdateUserRequest(
        String username,
        String email
) {
}

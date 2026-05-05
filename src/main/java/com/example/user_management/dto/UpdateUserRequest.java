package com.example.user_management.dto;

public record UpdateUserRequest(
        String username,
        String email
) {
}

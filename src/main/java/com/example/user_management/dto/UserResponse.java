package com.example.user_management.dto;

public record UserResponse(
        Integer id,
        String username,
        String email,
        boolean active,
        boolean isDeleted
) {
}

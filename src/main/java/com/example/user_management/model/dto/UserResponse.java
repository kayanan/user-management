package com.example.user_management.model.dto;

public record UserResponse(
        Integer id,
        String username,
        String email,
        boolean active,
        boolean isDeleted
) {
}

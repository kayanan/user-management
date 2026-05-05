package com.example.user_management.dto;

public record CreatePermissionRequest(
        String name,
        String description
) {
}

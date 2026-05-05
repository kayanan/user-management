package com.example.user_management.dto.request;

public record CreatePermissionRequest(
        String name,
        String description
) {
}

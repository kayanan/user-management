package com.example.user_management.dto.response;

public record PermissionResponse(
        Integer id,
        String name,
        String description,
        Boolean isActive,
        Boolean isDeleted
) {
}

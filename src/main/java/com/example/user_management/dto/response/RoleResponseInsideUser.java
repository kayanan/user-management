package com.example.user_management.dto.response;

import java.util.Set;

public record RoleResponseInsideUser(
        Integer id,
        String name,
        String description,
        Set<PermissionResponse> permissions,
        Boolean isActive,
        Boolean isDeleted
) {
}

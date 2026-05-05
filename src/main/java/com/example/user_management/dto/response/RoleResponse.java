package com.example.user_management.dto.response;

import com.example.user_management.entity.Permission;

import java.util.Set;

public record RoleResponse(
        Integer id,
        String name,
        String description,
        Set<String> permissions,
        Boolean isActive,
        Boolean isDeleted
) {
}

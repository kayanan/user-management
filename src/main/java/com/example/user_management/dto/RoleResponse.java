package com.example.user_management.dto;

import java.util.Set;

public record RoleResponse(
        Integer id,
        String name,
        String description,
        Set<String> permissions
) {
}

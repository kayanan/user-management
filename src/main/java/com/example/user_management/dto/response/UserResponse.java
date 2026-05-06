package com.example.user_management.dto.response;

import com.example.user_management.entity.Role;

public record UserResponse(
        Integer id,
        String username,
        String email,
        RoleResponse roles,
        boolean active,
        boolean isDeleted
) {
}

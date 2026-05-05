package com.example.user_management.dto.response;

import com.example.user_management.entity.Role;

import java.util.Set;

public record UserResponse(
        Integer id,
        String username,
        String email,
        Set<RoleResponseInsideUser> roles,
        boolean active,
        boolean isDeleted
) {
}

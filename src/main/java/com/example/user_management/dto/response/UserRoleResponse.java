package com.example.user_management.dto.response;

import java.util.Set;

public record UserRoleResponse(
        Integer userId,
        String username,
        String email,
        Set<String> roles
) {
}

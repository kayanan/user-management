package com.example.user_management.dto;

import java.util.Set;

public record AssignRoleRequest(
        Set<Integer> roleIds
) {
}

package com.example.user_management.dto.request;

import java.util.Set;

public record AssignRoleRequest(
        Set<Integer> roleIds
) {
}

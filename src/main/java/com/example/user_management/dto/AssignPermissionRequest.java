package com.example.user_management.dto;

import java.util.Set;

public record AssignPermissionRequest(
        Set<Integer> permissionIds
) {
}

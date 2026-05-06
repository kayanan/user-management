package com.example.user_management.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record AssignOrRemovePermissionRequest(
        @NotNull(message = "Permission IDs cannot be null")
        @NotEmpty(message = "Permission IDs cannot be empty")
        Set<@NotNull(message = "Permission ID cannot be null") Integer> permissionIds
) {
}

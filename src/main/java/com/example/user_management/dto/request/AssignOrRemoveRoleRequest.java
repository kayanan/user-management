package com.example.user_management.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AssignOrRemoveRoleRequest(
        @NotNull(message = "Permission IDs cannot be null")
        @NotEmpty(message = "Permission IDs cannot be empty")
        Integer roleId
) {
}

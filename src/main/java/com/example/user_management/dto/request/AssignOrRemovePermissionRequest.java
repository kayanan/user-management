package com.example.user_management.dto.request;

import java.util.Set;

public record AssignOrRemovePermissionRequest(
        Set<Integer> permissionIds
) {
}

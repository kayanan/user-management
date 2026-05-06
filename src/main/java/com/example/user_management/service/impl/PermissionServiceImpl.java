package com.example.user_management.service.impl;

import com.example.user_management.config.PermissionItem;
import com.example.user_management.dto.response.PermissionResponse;
import com.example.user_management.service.PermissionService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class PermissionServiceImpl implements PermissionService {

    private Map<String, List<PermissionItem>> permissions;

    @Override
    public List<PermissionResponse> getAllPermissions() {
        return getAllPermissionsAsSet()
                .stream()
                .map(this::mapToPermissionResponse)
                .toList();
    }

    @Override
    public PermissionResponse getPermissionById(Integer permissionId) {
        PermissionItem item = getAllPermissionsAsSet().stream()
                .filter(permissionItem -> permissionItem.id().equals(permissionId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Permission not found with Id :" + permissionId));
        return mapToPermissionResponse(item);
    }

    private Set<PermissionItem> getAllPermissionsAsSet() {
        Set<PermissionItem> allPermissions = new HashSet<>();
        permissions.forEach((kay, value) -> {
            allPermissions.addAll(value);
        });
        return allPermissions;
    }

    private PermissionResponse mapToPermissionResponse(PermissionItem permission) {
        return new PermissionResponse(
                permission.id(),
                permission.name(),
                permission.code()

        );
    }

}

package com.example.user_management.service;

import com.example.user_management.exceptions.PermissionAlreadyExistsException;
import com.example.user_management.exceptions.PermissionNotFoundException;
import com.example.user_management.dto.CreatePermissionRequest;
import com.example.user_management.dto.PermissionResponse;
import com.example.user_management.entity.Permission;
import com.example.user_management.repo.PermissionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepo permissionRepo;

    public PermissionResponse createPermission(CreatePermissionRequest request) {
        if (permissionRepo.existsByName(request.name())) {
            throw new PermissionAlreadyExistsException("Permission already exists: " + request.name());
        }

        Permission permission = new Permission();
        permission.setName(request.name());
        permission.setDescription(request.description());

        Permission savedPermission = permissionRepo.save(permission);

        return mapToPermissionResponse(savedPermission);
    }

    public List<PermissionResponse> getAllPermissions() {
        return permissionRepo.findAll()
                .stream()
                .map(this::mapToPermissionResponse)
                .toList();
    }

    public PermissionResponse getPermissionById(Integer permissionId) {
        Permission permission = findPermissionById(permissionId);
        return mapToPermissionResponse(permission);
    }

    public void deletePermission(Integer permissionId) {
        Permission permission = findPermissionById(permissionId);
        permissionRepo.delete(permission);
    }

    private Permission findPermissionById(Integer id) {
        return permissionRepo.findById(id)
                .orElseThrow(() -> new PermissionNotFoundException("Permission not found with id: " + id));
    }

    private PermissionResponse mapToPermissionResponse(Permission permission) {
        return new PermissionResponse(
                permission.getId(),
                permission.getName(),
                permission.getDescription()
        );
    }
}

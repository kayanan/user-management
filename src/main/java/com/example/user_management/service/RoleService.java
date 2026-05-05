package com.example.user_management.service;

import com.example.user_management.exceptions.RoleAlreadyExistsException;
import com.example.user_management.exceptions.RoleNotFoundException;
import com.example.user_management.dto.AssignPermissionRequest;
import com.example.user_management.dto.CreateRoleRequest;
import com.example.user_management.dto.RoleResponse;
import com.example.user_management.entity.Permission;
import com.example.user_management.entity.Role;
import com.example.user_management.repo.PermissionRepo;
import com.example.user_management.repo.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class RoleService {

    private final RoleRepo roleRepo;
    private final PermissionRepo permissionRepo;

    public RoleResponse createRole(CreateRoleRequest request) {
        if (roleRepo.existsByName(request.name())) {
            throw new RoleAlreadyExistsException("Role already exists: " + request.name());
        }

        Role role = new Role();
        role.setName(request.name());
        role.setDescription(request.description());

        Role savedRole = roleRepo.save(role);

        return mapToRoleResponse(savedRole);
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepo.findAll()
                .stream()
                .map(this::mapToRoleResponse)
                .toList();
    }

    public RoleResponse getRoleById(Integer roleId) {
        Role role = findRoleById(roleId);
        return mapToRoleResponse(role);
    }

    public RoleResponse assignPermissionsToRole(
            Integer roleId,
            AssignPermissionRequest request
    ) {
        Role role = findRoleById(roleId);

        Set<Permission> permissions = new HashSet<>(
                permissionRepo.findAllById(request.permissionIds())
        );

        if (permissions.size() != request.permissionIds().size()) {
            throw new RuntimeException("One or more permissions not found");
        }

        role.setPermissions(permissions);

        Role savedRole = roleRepo.save(role);

        return mapToRoleResponse(savedRole);
    }

    public RoleResponse addPermissionsToRole(
            Integer roleId,
            AssignPermissionRequest request
    ) {
        Role role = findRoleById(roleId);

        Set<Permission> permissions = new HashSet<>(
                permissionRepo.findAllById(request.permissionIds())
        );

        if (permissions.size() != request.permissionIds().size()) {
            throw new RuntimeException("One or more permissions not found");
        }

        role.getPermissions().addAll(permissions);

        Role savedRole = roleRepo.save(role);

        return mapToRoleResponse(savedRole);
    }

    public void deleteRole(Integer roleId) {
        Role role = findRoleById(roleId);
        roleRepo.delete(role);
    }

    private Role findRoleById(Integer roleId) {
        return roleRepo.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleId));
    }

    private RoleResponse mapToRoleResponse(Role role) {
        Set<String> permissions = role.getPermissions()
                .stream()
                .map(Permission::getName)
                .collect(java.util.stream.Collectors.toSet());

        return new RoleResponse(
                role.getId(),
                role.getName(),
                role.getDescription(),
                permissions
        );
    }
}

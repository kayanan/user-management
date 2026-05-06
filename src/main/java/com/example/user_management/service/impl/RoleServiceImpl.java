package com.example.user_management.service.impl;

import com.example.user_management.dto.request.AssignOrRemovePermissionRequest;
import com.example.user_management.exceptions.RoleAlreadyExistsException;
import com.example.user_management.dto.request.CreateRoleRequest;
import com.example.user_management.dto.response.RoleResponse;
import com.example.user_management.entity.Permission;
import com.example.user_management.entity.Role;
import com.example.user_management.exceptions.RoleNotFoundException;
import com.example.user_management.repo.PermissionRepo;
import com.example.user_management.repo.RoleRepo;
import com.example.user_management.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;
    private final PermissionRepo permissionRepo;

    @Override
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

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepo.findAll()
                .stream()
                .filter(role -> !role.isDeleted())
                .map(this::mapToRoleResponse)
                .toList();
    }

    @Override
    public RoleResponse getRoleById(Integer roleId) {
        Role role = findRoleById(roleId);
        return mapToRoleResponse(role);
    }

    @Override
    public RoleResponse assignPermissionsToRole(
            Integer roleId,
            AssignOrRemovePermissionRequest request
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

    @Override
    public RoleResponse addPermissionsToRole(
            Integer roleId,
            AssignOrRemovePermissionRequest request
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

    @Override
    public RoleResponse removePermissionsFromRole(
            Integer roleId,
            AssignOrRemovePermissionRequest request
    ) {
        Role role = findRoleById(roleId);

        Set<Permission> permissions = new HashSet<>(
                permissionRepo.findAllById(request.permissionIds())
        );

        if (permissions.size() != request.permissionIds().size()) {
            throw new RuntimeException("One or more permissions not found");
        }

        Set<Permission> filteredPermissions = role.getPermissions()
                .stream()
                .filter(permission -> !request.permissionIds().contains(permission.getId()))
                .collect(Collectors.toSet());
        role.setPermissions(filteredPermissions);
        Role savedRole = roleRepo.save(role);

        return mapToRoleResponse(savedRole);
    }

    @Override
    public RoleResponse softDeleteRole(Integer roleId) {
        Role role = findRoleById(roleId);
        role.setDeleted(true);
        role.setActive(false);
        return mapToRoleResponse(roleRepo.save(role));
    }

    @Override
    public void hardDeleteRole(Integer roleId) {
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
                permissions,
                role.isActive(),
                role.isDeleted()
        );
    }

}

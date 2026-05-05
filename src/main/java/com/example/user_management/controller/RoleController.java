package com.example.user_management.controller;

import com.example.user_management.dto.request.AssignOrRemovePermissionRequest;
import com.example.user_management.dto.request.CreateRoleRequest;
import com.example.user_management.dto.response.RoleResponse;
import com.example.user_management.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleResponse> createRole(
            @RequestBody CreateRoleRequest request
    ) {
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleResponse> getRoleById(
            @PathVariable Integer roleId
    ) {
        return ResponseEntity.ok(roleService.getRoleById(roleId));
    }

    @PutMapping("/{roleId}/permissions")
    public ResponseEntity<RoleResponse> assignPermissionsToRole(
            @PathVariable Integer roleId,
            @RequestBody AssignOrRemovePermissionRequest request
    ) {
        return ResponseEntity.ok(roleService.assignPermissionsToRole(roleId, request));
    }

    @PatchMapping("/{roleId}/permissions/add")
    public ResponseEntity<RoleResponse> addPermissionsToRole(
            @PathVariable Integer roleId,
            @RequestBody AssignOrRemovePermissionRequest request
    ) {
        return ResponseEntity.ok(roleService.addPermissionsToRole(roleId, request));
    }

    @PatchMapping("/{roleId}/permissions/remove")
    public ResponseEntity<RoleResponse> removePermissionsToRole(
            @PathVariable Integer roleId,
            @RequestBody AssignOrRemovePermissionRequest request
    ) {
        return ResponseEntity.ok(roleService.removePermissionsFromRole(roleId, request));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<RoleResponse> softDeleteRole(
            @PathVariable Integer roleId
    ) {
        return ResponseEntity.ok( roleService.softDeleteRole(roleId));
    }

    @DeleteMapping("/{roleId}/hard")
    public ResponseEntity<Void> hardDeleteRole(
            @PathVariable Integer roleId
    ) {
        roleService.hardDeleteRole(roleId);
        return ResponseEntity.noContent().build();
    }
}

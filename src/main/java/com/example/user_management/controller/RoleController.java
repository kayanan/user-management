package com.example.user_management.controller;

import com.example.user_management.dto.AssignPermissionRequest;
import com.example.user_management.dto.CreateRoleRequest;
import com.example.user_management.dto.RoleResponse;
import com.example.user_management.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
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
            @RequestBody AssignPermissionRequest request
    ) {
        return ResponseEntity.ok(roleService.assignPermissionsToRole(roleId, request));
    }

    @PatchMapping("/{roleId}/permissions")
    public ResponseEntity<RoleResponse> addPermissionsToRole(
            @PathVariable Integer roleId,
            @RequestBody AssignPermissionRequest request
    ) {
        return ResponseEntity.ok(roleService.addPermissionsToRole(roleId, request));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(
            @PathVariable Integer roleId
    ) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }
}

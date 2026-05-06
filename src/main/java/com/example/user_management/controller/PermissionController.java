package com.example.user_management.controller;

import com.example.user_management.dto.request.CreatePermissionRequest;
import com.example.user_management.dto.response.PermissionResponse;
import com.example.user_management.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<PermissionResponse> createPermission(
            @Valid @RequestBody CreatePermissionRequest request
    ) {
        return ResponseEntity.ok(permissionService.createPermission(request));
    }

    @GetMapping
    public ResponseEntity<List<PermissionResponse>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    @GetMapping("/{permissionId}")
    public ResponseEntity<PermissionResponse> getPermissionById(
            @PathVariable Integer permissionId
    ) {
        return ResponseEntity.ok(permissionService.getPermissionById(permissionId));
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<PermissionResponse> softDeletePermission(
            @PathVariable Integer permissionId
    ) {
        return ResponseEntity.ok(permissionService.softDeletePermission(permissionId));
    }

    @DeleteMapping("/{permissionId}/hard")
    public ResponseEntity<Void> hardDeleteUser(@PathVariable Integer permissionId) {
        permissionService.hardDeletePermission(permissionId);
        return ResponseEntity.noContent().build();
    }
}

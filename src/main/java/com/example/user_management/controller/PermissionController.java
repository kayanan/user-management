package com.example.user_management.controller;

import com.example.user_management.dto.CreatePermissionRequest;
import com.example.user_management.dto.PermissionResponse;
import com.example.user_management.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<PermissionResponse> createPermission(
            @RequestBody CreatePermissionRequest request
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
    public ResponseEntity<Void> deletePermission(
            @PathVariable Integer permissionId
    ) {
        permissionService.deletePermission(permissionId);
        return ResponseEntity.noContent().build();
    }
}

package com.example.user_management.controller;

import com.example.user_management.dto.response.ApiResponse;
import com.example.user_management.dto.response.PermissionResponse;
import com.example.user_management.service.PermissionService;
import com.example.user_management.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAllPermissions() {
        return ResponseUtil.success( "Data retrieved successfully",permissionService.getAllPermissions());
    }

    @GetMapping("/{permissionId}")
    public ResponseEntity<ApiResponse<PermissionResponse>> getPermissionById(
            @PathVariable Integer permissionId
    ) {
        return ResponseUtil.success("Data retrieved successfully",permissionService.getPermissionById(permissionId));
    }

}

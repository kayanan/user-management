package com.example.user_management.service;

import com.example.user_management.dto.request.CreatePermissionRequest;
import com.example.user_management.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse createPermission(CreatePermissionRequest request);

    List<PermissionResponse> getAllPermissions();

    PermissionResponse getPermissionById(Integer permissionId);

    PermissionResponse softDeletePermission(Integer permissionId);

    void hardDeletePermission(Integer permissionId);

}

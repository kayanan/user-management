package com.example.user_management.service;

import com.example.user_management.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {

    List<PermissionResponse> getAllPermissions();

    PermissionResponse getPermissionById(Integer permissionId);


}

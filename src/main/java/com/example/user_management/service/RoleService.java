package com.example.user_management.service;

import com.example.user_management.dto.request.AssignOrRemovePermissionRequest;
import com.example.user_management.dto.request.CreateRoleRequest;
import com.example.user_management.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse createRole(CreateRoleRequest request);

    List<RoleResponse> getAllRoles();

    RoleResponse getRoleById(Integer roleId);

    RoleResponse assignPermissionsToRole(
            Integer roleId,
            AssignOrRemovePermissionRequest request
    );

    RoleResponse addPermissionsToRole(
            Integer roleId,
            AssignOrRemovePermissionRequest request
    );

    RoleResponse removePermissionsFromRole(
            Integer roleId,
            AssignOrRemovePermissionRequest request
    );

    RoleResponse softDeleteRole(Integer roleId);

    void hardDeleteRole(Integer roleId);

}

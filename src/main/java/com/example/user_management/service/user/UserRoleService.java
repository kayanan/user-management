package com.example.user_management.service.user;

import com.example.user_management.dto.request.AssignRoleRequest;
import com.example.user_management.dto.response.UserRoleResponse;

public interface UserRoleService {
    UserRoleResponse assignRolesToUser(
            Integer userId,
            AssignRoleRequest request
    );

    UserRoleResponse addRolesToUser(
            Integer userId,
            AssignRoleRequest request
    );

    UserRoleResponse removeRoleFromUser(Integer userId, Integer roleId);

    UserRoleResponse getUserRoles(Integer userId);

}

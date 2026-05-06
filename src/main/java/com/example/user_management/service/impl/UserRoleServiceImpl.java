package com.example.user_management.service.impl;

import com.example.user_management.dto.request.AssignRoleRequest;
import com.example.user_management.dto.response.UserRoleResponse;
import com.example.user_management.entity.Role;
import com.example.user_management.entity.user.User;
import com.example.user_management.repo.RoleRepo;
import com.example.user_management.repo.UserRepo;
import com.example.user_management.service.user.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;

    @Override
    public UserRoleResponse assignRolesToUser(
            Integer userId,
            AssignRoleRequest request
    ) {
        User user = findUserById(userId);

        Set<Role> roles = new HashSet<>(
                roleRepo.findAllById(request.roleIds())
        );

        if (roles.size() != request.roleIds().size()) {
            throw new RuntimeException("One or more roles not found");
        }

        user.setRoles(roles);

        User savedUser = userRepo.save(user);

        return mapToUserRoleResponse(savedUser);
    }

    @Override
    public UserRoleResponse addRolesToUser(
            Integer userId,
            AssignRoleRequest request
    ) {
        User user = findUserById(userId);

        Set<Role> roles = new HashSet<>(
                roleRepo.findAllById(request.roleIds())
        );

        if (roles.size() != request.roleIds().size()) {
            throw new RuntimeException("One or more roles not found");
        }

        user.getRoles().addAll(roles);

        User savedUser = userRepo.save(user);

        return mapToUserRoleResponse(savedUser);
    }

    @Override
    public UserRoleResponse removeRoleFromUser(Integer userId, Integer roleId) {
        User user = findUserById(userId);

        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        user.getRoles().remove(role);

        User savedUser = userRepo.save(user);

        return mapToUserRoleResponse(savedUser);
    }

    @Override
    public UserRoleResponse getUserRoles(Integer userId) {
        User user = findUserById(userId);
        return mapToUserRoleResponse(user);
    }

    private User findUserById(Integer userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    private UserRoleResponse mapToUserRoleResponse(User user) {
        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new UserRoleResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                roles
        );
    }

}

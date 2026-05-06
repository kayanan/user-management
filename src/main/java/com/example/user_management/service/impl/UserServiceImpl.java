package com.example.user_management.service.impl;

import com.example.user_management.dto.response.PermissionResponse;
import com.example.user_management.dto.response.RoleResponse;
import com.example.user_management.entity.Role;
import com.example.user_management.exceptions.RoleNotFoundException;
import com.example.user_management.exceptions.UserAlreadyExistsException;
import com.example.user_management.entity.user.User;
import com.example.user_management.dto.request.UpdateUserRequest;
import com.example.user_management.dto.request.UserRegisterRequest;
import com.example.user_management.dto.response.UserResponse;
import com.example.user_management.exceptions.UserNotFoundException;
import com.example.user_management.repo.RoleRepo;
import com.example.user_management.repo.UserRepo;
import com.example.user_management.service.PermissionService;
import com.example.user_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    private final PermissionService permissionService;

    @Override
    public UserResponse saveUser(UserRegisterRequest userRegisterRequest) {
        User existingUser = userRepo.findByEmail(userRegisterRequest.email()).orElse(null);
        if (existingUser != null) {
            throw new UserAlreadyExistsException("User already exists with email : " + userRegisterRequest.email());
        }
        User user = User.builder()
                .username(userRegisterRequest.username())
                .email(userRegisterRequest.email())
                .password(encoder.encode(userRegisterRequest.password()))
                .build();

        return mapToResponse(userRepo.save(user));

    }

    @Override
    public UserResponse getUserById(Integer id) {
        User user = findUserById(id);
        return mapToResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .filter((user -> !user.isDeleted()))
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserResponse updateUser(Integer id, UpdateUserRequest request) {
        User user = findUserById(id);
        if (request.username() != null) {
            user.setUsername(request.username());
        }
        if (request.email() != null) {
            user.setEmail(request.email());
        }
        User updatedUser = userRepo.save(user);
        return mapToResponse(updatedUser);
    }

    @Override
    public UserResponse deactivateUser(Integer id) {
        User user = findUserById(id);
        user.setActive(false);
        User updatedUser = userRepo.save(user);
        return mapToResponse(updatedUser);
    }

    @Override
    public UserResponse activateUser(Integer id) {
        User user = findUserById(id);
        user.setActive(true);
        User updatedUser = userRepo.save(user);
        return mapToResponse(updatedUser);
    }

    @Override
    public UserResponse assignRoleToUser(
            Integer userId,
            Integer roleId
    ) {
        User user = findUserById(userId);
        Role role = roleRepo.findById(
                        roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found for roleId :" + roleId
                ));
        user.setRole(role);
        User savedUser = userRepo.save(user);
        return mapToResponse(savedUser);
    }

    @Override
    public UserResponse removeRoleFromUser(
            Integer userId,
            Integer roleId
    ) {
        User user = findUserById(userId);
        Role role = roleRepo.findById(
                        roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found for roleId :" + roleId
                ));
        if (user.getRole().equals(role)) {
            user.setRole(null);
        } else {
            throw new RuntimeException("User does not have role: " + role.getName());
        }

        User savedUser = userRepo.save(user);
        return mapToResponse(savedUser);
    }

    @Override
    public UserResponse softDeleteUser(Integer id) {
        User user = findUserById(id);
        user.setDeleted(true);
        user.setActive(false);
        User deletedUser = userRepo.save(user);
        return mapToResponse(deletedUser);
    }

    @Override
    public Void hardDeleteUser(Integer id) {
        User user = findUserById(id);
        userRepo.delete(user);
        return null;
    }

    private User findUserById(Integer id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    private UserResponse mapToResponse(User user) {
        List<PermissionResponse> permissions = permissionService.getAllPermissions();
        Set<PermissionResponse> filteredPermissions = null;
        Role role = user.getRole();

        if (role != null && role.getPermissionIds().length > 0) {
            filteredPermissions = permissions.stream().filter(
                    permissionResponse ->
                            Arrays.asList(role.getPermissionIds()).contains(permissionResponse.id())
            ).collect(Collectors.toSet());

        }
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                role == null ? null :
                        new RoleResponse(
                                role.getId(),
                                role.getName(),
                                role.getDescription(),
                                filteredPermissions,
                                role.isActive(),
                                role.isDeleted()
                        )
                ,
                user.isActive(),
                user.isDeleted()
        );
    }

}
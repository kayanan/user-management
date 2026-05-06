package com.example.user_management.service.impl;

import com.example.user_management.dto.response.PermissionResponse;
import com.example.user_management.dto.response.RoleResponseInsideUser;
import com.example.user_management.exceptions.UserAlreadyExistsException;
import com.example.user_management.entity.user.User;
import com.example.user_management.dto.request.UpdateUserRequest;
import com.example.user_management.dto.request.UserRegisterRequest;
import com.example.user_management.dto.response.UserResponse;
import com.example.user_management.exceptions.UserNotFoundException;
import com.example.user_management.repo.UserRepo;
import com.example.user_management.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

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
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRoles().stream().map(role -> new RoleResponseInsideUser(
                        role.getId(),
                        role.getName(),
                        role.getDescription(),
                        role.getPermissions().stream().map(permission -> new PermissionResponse(
                                permission.getId(),
                                permission.getName(),
                                permission.getDescription(),
                                permission.isActive(),
                                permission.isDeleted()
                        )).collect(Collectors.toSet()),
                        role.isActive(),
                        role.isDeleted()
                )).collect(Collectors.toSet()),
                user.isActive(),
                user.isDeleted()
        );
    }

}

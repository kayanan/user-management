package com.example.user_management.service;

import com.example.user_management.exceptions.UserNotFoundException;
import com.example.user_management.model.User;
import com.example.user_management.model.dto.UpdateUserRequest;
import com.example.user_management.model.dto.UserRegisterRequest;
import com.example.user_management.model.dto.UserResponse;
import com.example.user_management.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserResponse saveUser(UserRegisterRequest userRegisterRequest) {
        User user = User.builder()
                .username(userRegisterRequest.username())
                .email(userRegisterRequest.email())
                .password(encoder.encode(userRegisterRequest.password()))
                .isDeleted(false)
                .isActive(true)
                .build();

        return mapToResponse(userRepo.save(user));

    }

    public UserResponse getUserById(Integer id) {
        User user = findUserById(id);
        return mapToResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepo.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

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

    public UserResponse deactivateUser(Integer id) {
        User user = findUserById(id);
        user.setActive(false);
        User updatedUser = userRepo.save(user);
        return mapToResponse(updatedUser);
    }

    public UserResponse activateUser(Integer id) {
        User user = findUserById(id);
        user.setActive(true);
        User updatedUser = userRepo.save(user);
        return mapToResponse(updatedUser);
    }

    public UserResponse softDeleteUser(Integer id) {
        User user = findUserById(id);
        user.setDeleted(true);
        User deletedUser = userRepo.save(user);
        return mapToResponse(deletedUser);
    }

    public Void hardDeleteUser(Integer id) {
        userRepo.deleteById(id);
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
                user.isActive(),
                user.isDeleted()
        );
    }

}

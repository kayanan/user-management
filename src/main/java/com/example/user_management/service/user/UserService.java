package com.example.user_management.service.user;

import com.example.user_management.exceptions.UserNotFoundException;
import com.example.user_management.entity.user.User;
import com.example.user_management.dto.UpdateUserRequest;
import com.example.user_management.dto.UserRegisterRequest;
import com.example.user_management.dto.UserResponse;
import com.example.user_management.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserResponse saveUser(UserRegisterRequest userRegisterRequest) {
        User user = User.builder()
                .username(userRegisterRequest.username())
                .email(userRegisterRequest.email())
                .password(encoder.encode(userRegisterRequest.password()))
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
                .filter((user -> !user.isDeleted()))
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
        user.setActive(false);
        User deletedUser = userRepo.save(user);
        return mapToResponse(deletedUser);
    }

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
                user.isActive(),
                user.isDeleted()
        );
    }

}

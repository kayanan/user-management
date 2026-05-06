package com.example.user_management.service.user;

import com.example.user_management.dto.request.UpdateUserRequest;
import com.example.user_management.dto.request.UserRegisterRequest;
import com.example.user_management.dto.response.UserResponse;

import java.util.List;


public interface UserService {
    UserResponse saveUser(UserRegisterRequest userRegisterRequest);

    UserResponse getUserById(Integer id);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Integer id, UpdateUserRequest request);

    UserResponse deactivateUser(Integer id);

    UserResponse activateUser(Integer id);

    UserResponse softDeleteUser(Integer id);

    Void hardDeleteUser(Integer id);

}

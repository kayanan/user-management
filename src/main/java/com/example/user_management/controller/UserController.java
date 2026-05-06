package com.example.user_management.controller;

import com.example.user_management.dto.request.AssignOrRemoveRoleRequest;
import com.example.user_management.dto.request.UpdateUserRequest;
import com.example.user_management.dto.response.ApiResponse;
import com.example.user_management.dto.response.UserResponse;
import com.example.user_management.service.UserService;
import com.example.user_management.utils.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/users")
public class UserController {


    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Integer id) {

        UserResponse user = userService.getUserById(id);
        return ResponseUtil.success(HttpStatus.CREATED,"User fetched successfully",user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Integer id, @Valid @RequestBody UpdateUserRequest request) {
        UserResponse updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<UserResponse> deactivateUser(@PathVariable Integer id) {
        UserResponse user = userService.deactivateUser(id);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<UserResponse> activateUser(@PathVariable Integer id) {
        UserResponse user = userService.activateUser(id);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/{userId}/roles/assign")
    public ResponseEntity<ApiResponse<Void>> assignRoles(
            @PathVariable Integer userId,
            @RequestBody AssignOrRemoveRoleRequest request) {

        userService.assignRoleToUser(userId, request.roleId());
        return ResponseUtil.success("Roles assigned successfully", null);
    }

    @PutMapping("/{userId}/roles/remove")
    public ResponseEntity<ApiResponse<Void>> removeRoles(
            @PathVariable Integer userId,
            @RequestBody AssignOrRemoveRoleRequest request) {

        userService.removeRoleFromUser(userId, request.roleId());
        return ResponseUtil.success("Roles removed successfully", null);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> softDeleteUser(@PathVariable Integer id) {
        UserResponse userResponse= userService.softDeleteUser(id);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<Void> hardDeleteUser(@PathVariable Integer id) {
        userService.hardDeleteUser(id);
        return ResponseEntity.noContent().build();
    }



}

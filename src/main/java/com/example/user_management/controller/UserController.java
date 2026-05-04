package com.example.user_management.controller;

import com.example.user_management.model.dto.UpdateUserRequest;
import com.example.user_management.model.dto.UserResponse;
import com.example.user_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {

        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Integer id, @RequestBody UpdateUserRequest request) {
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

package com.example.user_management.controller.user;

import com.example.user_management.dto.request.AssignRoleRequest;
import com.example.user_management.dto.response.UserRoleResponse;
import com.example.user_management.service.user.UserRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @GetMapping("/{userId}/roles")
    public ResponseEntity<UserRoleResponse> getUserRoles(
            @PathVariable Integer userId
    ) {
        return ResponseEntity.ok(userRoleService.getUserRoles(userId));
    }

    @PutMapping("/{userId}/roles")
    public ResponseEntity<UserRoleResponse> assignRolesToUser(
            @PathVariable Integer userId,
            @Valid @RequestBody AssignRoleRequest request
    ) {
        return ResponseEntity.ok(userRoleService.assignRolesToUser(userId, request));
    }

    @PatchMapping("/{userId}/roles")
    public ResponseEntity<UserRoleResponse> addRolesToUser(
            @PathVariable Integer userId,
            @Valid @RequestBody AssignRoleRequest request
    ) {
        return ResponseEntity.ok(userRoleService.addRolesToUser(userId, request));
    }

    @DeleteMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<UserRoleResponse> removeRoleFromUser(
            @PathVariable Integer userId,
            @PathVariable Integer roleId
    ) {
        return ResponseEntity.ok(userRoleService.removeRoleFromUser(userId, roleId));
    }
}

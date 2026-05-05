package com.example.user_management.service.userRole;

import com.example.user_management.dto.request.AssignRoleRequest;
import com.example.user_management.dto.response.UserRoleResponse;
import com.example.user_management.entity.Role;
import com.example.user_management.entity.user.User;
import com.example.user_management.repo.RoleRepo;
import com.example.user_management.repo.UserRepo;
import com.example.user_management.service.user.UserRoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRoleServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private RoleRepo roleRepo;

    private UserRoleService userRoleService;

    private User user;
    private Role adminRole;
    private Role userRole;

    @BeforeEach
    void setUp() {
        userRoleService = new UserRoleService(userRepo, roleRepo);

        adminRole = new Role();
        adminRole.setId(1);
        adminRole.setName("ADMIN");
        adminRole.setDescription("Admin role");

        userRole = new Role();
        userRole.setId(2);
        userRole.setName("USER");
        userRole.setDescription("User role");

        user = User.builder()
                .id(1)
                .username("ragu")
                .email("ragu@gmail.com")
                .roles(new HashSet<>())
                .build();
    }

    @Test
    void shouldAssignRolesToUserSuccessfully() {
        Integer userId = 1;

        AssignRoleRequest request = new AssignRoleRequest(Set.of(1, 2));

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepo.findAllById(request.roleIds()))
                .thenReturn(List.of(adminRole, userRole));

        User savedUser = User.builder()
                .id(1)
                .username("ragu")
                .email("ragu@gmail.com")
                .roles(Set.of(adminRole, userRole))
                .build();

        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        UserRoleResponse response = userRoleService.assignRolesToUser(userId, request);

        assertNotNull(response);
        assertEquals(1, response.userId());
        assertEquals("ragu", response.username());
        assertEquals("ragu@gmail.com", response.email());
        assertEquals(2, response.roles().size());
        assertTrue(response.roles().contains("ADMIN"));
        assertTrue(response.roles().contains("USER"));

        verify(userRepo, times(1)).findById(userId);
        verify(roleRepo, times(1)).findAllById(request.roleIds());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenAssigningRolesAndSomeRolesNotFound() {
        Integer userId = 1;

        AssignRoleRequest request = new AssignRoleRequest(Set.of(1, 2));

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepo.findAllById(request.roleIds()))
                .thenReturn(List.of(adminRole));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userRoleService.assignRolesToUser(userId, request)
        );

        assertEquals("One or more roles not found", exception.getMessage());

        verify(userRepo, times(1)).findById(userId);
        verify(roleRepo, times(1)).findAllById(request.roleIds());
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void shouldAddRolesToUserSuccessfully() {
        Integer userId = 1;

        user.setRoles(new HashSet<>(Set.of(userRole)));

        AssignRoleRequest request = new AssignRoleRequest(Set.of(1));

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepo.findAllById(request.roleIds()))
                .thenReturn(List.of(adminRole));

        User savedUser = User.builder()
                .id(1)
                .username("ragu")
                .email("ragu@gmail.com")
                .roles(Set.of(userRole, adminRole))
                .build();

        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        UserRoleResponse response = userRoleService.addRolesToUser(userId, request);

        assertNotNull(response);
        assertEquals(2, response.roles().size());
        assertTrue(response.roles().contains("ADMIN"));
        assertTrue(response.roles().contains("USER"));

        verify(userRepo, times(1)).findById(userId);
        verify(roleRepo, times(1)).findAllById(request.roleIds());
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenAddingRolesAndSomeRolesNotFound() {
        Integer userId = 1;

        AssignRoleRequest request = new AssignRoleRequest(Set.of(1, 2));

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepo.findAllById(request.roleIds()))
                .thenReturn(List.of(adminRole));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userRoleService.addRolesToUser(userId, request)
        );

        assertEquals("One or more roles not found", exception.getMessage());

        verify(userRepo, times(1)).findById(userId);
        verify(roleRepo, times(1)).findAllById(request.roleIds());
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void shouldRemoveRoleFromUserSuccessfully() {
        Integer userId = 1;
        Integer roleId = 1;

        user.setRoles(new HashSet<>(Set.of(adminRole, userRole)));

        User savedUser = User.builder()
                .id(1)
                .username("ragu")
                .email("ragu@gmail.com")
                .roles(Set.of(userRole))
                .build();

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepo.findById(roleId)).thenReturn(Optional.of(adminRole));
        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        UserRoleResponse response = userRoleService.removeRoleFromUser(userId, roleId);

        assertNotNull(response);
        assertEquals(1, response.roles().size());
        assertFalse(response.roles().contains("ADMIN"));
        assertTrue(response.roles().contains("USER"));

        verify(userRepo, times(1)).findById(userId);
        verify(roleRepo, times(1)).findById(roleId);
        verify(userRepo, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenRemovingRoleAndRoleNotFound() {
        Integer userId = 1;
        Integer roleId = 99;

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepo.findById(roleId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userRoleService.removeRoleFromUser(userId, roleId)
        );

        assertEquals("Role not found with id: 99", exception.getMessage());

        verify(userRepo, times(1)).findById(userId);
        verify(roleRepo, times(1)).findById(roleId);
        verify(userRepo, never()).save(any(User.class));
    }

    @Test
    void shouldGetUserRolesSuccessfully() {
        Integer userId = 1;

        user.setRoles(Set.of(adminRole, userRole));

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        UserRoleResponse response = userRoleService.getUserRoles(userId);

        assertNotNull(response);
        assertEquals(1, response.userId());
        assertEquals("ragu", response.username());
        assertEquals("ragu@gmail.com", response.email());
        assertEquals(2, response.roles().size());
        assertTrue(response.roles().contains("ADMIN"));
        assertTrue(response.roles().contains("USER"));

        verify(userRepo, times(1)).findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        Integer userId = 99;

        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> userRoleService.getUserRoles(userId)
        );

        assertEquals("User not found with id: 99", exception.getMessage());

        verify(userRepo, times(1)).findById(userId);
    }
}
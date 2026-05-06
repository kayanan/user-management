package com.example.user_management.service.permission;

import com.example.user_management.exceptions.PermissionNotFoundException;
import com.example.user_management.dto.response.PermissionResponse;
import com.example.user_management.service.PermissionService;
import com.example.user_management.service.impl.PermissionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

    @Mock
    private PermissionRepo permissionRepo;

    private PermissionService permissionService;

    private Permission permission;

    @BeforeEach
    void setUp() {
        permissionService = new PermissionServiceImpl(permissionRepo);

        permission = new Permission();
        permission.setId(1);
        permission.setName("USER_READ");
        permission.setDescription("Can read users");
    }

    @Test
    void shouldCreatePermissionSuccessfully() {
        CreatePermissionRequest request = new CreatePermissionRequest(
                "USER_READ",
                "Can read users"
        );

        when(permissionRepo.existsByName(request.name())).thenReturn(false);
        when(permissionRepo.save(any(Permission.class))).thenReturn(permission);

        PermissionResponse response = permissionService.createPermission(request);

        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals("USER_READ", response.name());
        assertEquals("Can read users", response.description());

        verify(permissionRepo, times(1)).existsByName(request.name());
        verify(permissionRepo, times(1)).save(any(Permission.class));
    }

    @Test
    void shouldThrowExceptionWhenPermissionAlreadyExists() {
        CreatePermissionRequest request = new CreatePermissionRequest(
                "USER_READ",
                "Can read users"
        );

        when(permissionRepo.existsByName(request.name())).thenReturn(true);

        PermissionAlreadyExistsException exception = assertThrows(
                PermissionAlreadyExistsException.class,
                () -> permissionService.createPermission(request)
        );

        assertEquals("Permission already exists: USER_READ", exception.getMessage());

        verify(permissionRepo, times(1)).existsByName(request.name());
        verify(permissionRepo, never()).save(any(Permission.class));
    }

    @Test
    void shouldGetAllPermissionsSuccessfully() {
        Permission permission2 = new Permission();
        permission2.setId(2);
        permission2.setName("USER_CREATE");
        permission2.setDescription("Can create users");

        when(permissionRepo.findAll()).thenReturn(List.of(permission, permission2));

        List<PermissionResponse> responses = permissionService.getAllPermissions();

        assertEquals(2, responses.size());

        assertEquals(1, responses.get(0).id());
        assertEquals("USER_READ", responses.get(0).name());

        assertEquals(2, responses.get(1).id());
        assertEquals("USER_CREATE", responses.get(1).name());

        verify(permissionRepo, times(1)).findAll();
    }

    @Test
    void shouldGetPermissionByIdSuccessfully() {
        Integer permissionId = 1;

        when(permissionRepo.findById(permissionId)).thenReturn(Optional.of(permission));

        PermissionResponse response = permissionService.getPermissionById(permissionId);

        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals("USER_READ", response.name());
        assertEquals("Can read users", response.description());

        verify(permissionRepo, times(1)).findById(permissionId);
    }

    @Test
    void shouldThrowExceptionWhenPermissionNotFoundById() {
        Integer permissionId = 99;

        when(permissionRepo.findById(permissionId)).thenReturn(Optional.empty());

        PermissionNotFoundException exception = assertThrows(
                PermissionNotFoundException.class,
                () -> permissionService.getPermissionById(permissionId)
        );

        assertEquals("Permission not found with id: 99", exception.getMessage());

        verify(permissionRepo, times(1)).findById(permissionId);
    }

    @Test
    void shouldDeletePermissionSuccessfully() {
        Integer permissionId = 1;

        when(permissionRepo.findById(permissionId)).thenReturn(Optional.of(permission));
        doNothing().when(permissionRepo).delete(permission);

        permissionService.hardDeletePermission(permissionId);

        verify(permissionRepo, times(1)).findById(permissionId);
        verify(permissionRepo, times(1)).delete(permission);
    }

    @Test
    void shouldThrowExceptionWhenDeletingPermissionNotFound() {
        Integer permissionId = 99;

        when(permissionRepo.findById(permissionId)).thenReturn(Optional.empty());

        PermissionNotFoundException exception = assertThrows(
                PermissionNotFoundException.class,
                () -> permissionService.hardDeletePermission(permissionId)
        );

        assertEquals("Permission not found with id: 99", exception.getMessage());

        verify(permissionRepo, times(1)).findById(permissionId);
        verify(permissionRepo, never()).delete(any(Permission.class));
    }
}
package com.example.user_management.service.role;

import com.example.user_management.exceptions.RoleAlreadyExistsException;
import com.example.user_management.exceptions.RoleNotFoundException;
import com.example.user_management.dto.request.AssignOrRemovePermissionRequest;
import com.example.user_management.dto.request.CreateRoleRequest;
import com.example.user_management.dto.response.RoleResponse;
import com.example.user_management.entity.Permission;
import com.example.user_management.entity.Role;
import com.example.user_management.repo.PermissionRepo;
import com.example.user_management.repo.RoleRepo;
import com.example.user_management.service.RoleService;
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
class RoleServiceTest {

    @Mock
    private RoleRepo roleRepo;

    @Mock
    private PermissionRepo permissionRepo;

    private RoleService roleService;

    private Role role;
    private Permission permission1;
    private Permission permission2;

    @BeforeEach
    void setUp() {
        roleService = new RoleService(roleRepo, permissionRepo);

        permission1 = new Permission();
        permission1.setId(1);
        permission1.setName("USER_READ");
        permission1.setDescription("Can read users");

        permission2 = new Permission();
        permission2.setId(2);
        permission2.setName("USER_CREATE");
        permission2.setDescription("Can create users");

        role = new Role();
        role.setId(1);
        role.setName("ADMIN");
        role.setDescription("System administrator");
        role.setPermissions(new HashSet<>());
    }

    @Test
    void shouldCreateRoleSuccessfully() {
        CreateRoleRequest request = new CreateRoleRequest(
                "ADMIN",
                "System administrator"
        );

        when(roleRepo.existsByName(request.name())).thenReturn(false);
        when(roleRepo.save(any(Role.class))).thenReturn(role);

        RoleResponse response = roleService.createRole(request);

        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals("ADMIN", response.name());
        assertEquals("System administrator", response.description());
        assertTrue(response.permissions().isEmpty());

        verify(roleRepo, times(1)).existsByName(request.name());
        verify(roleRepo, times(1)).save(any(Role.class));
    }

    @Test
    void shouldThrowExceptionWhenRoleAlreadyExists() {
        CreateRoleRequest request = new CreateRoleRequest(
                "ADMIN",
                "System administrator"
        );

        when(roleRepo.existsByName(request.name())).thenReturn(true);

        RoleAlreadyExistsException exception = assertThrows(
                RoleAlreadyExistsException.class,
                () -> roleService.createRole(request)
        );

        assertEquals("Role already exists: ADMIN", exception.getMessage());

        verify(roleRepo, times(1)).existsByName(request.name());
        verify(roleRepo, never()).save(any(Role.class));
    }

    @Test
    void shouldGetAllRolesSuccessfully() {
        Role managerRole = new Role();
        managerRole.setId(2);
        managerRole.setName("MANAGER");
        managerRole.setDescription("Manager role");
        managerRole.setPermissions(Set.of(permission1));

        role.setPermissions(Set.of(permission1, permission2));

        when(roleRepo.findAll()).thenReturn(List.of(role, managerRole));

        List<RoleResponse> responses = roleService.getAllRoles();

        assertEquals(2, responses.size());

        assertEquals("ADMIN", responses.get(0).name());
        assertEquals(2, responses.get(0).permissions().size());

        assertEquals("MANAGER", responses.get(1).name());
        assertEquals(1, responses.get(1).permissions().size());

        verify(roleRepo, times(1)).findAll();
    }

    @Test
    void shouldGetRoleByIdSuccessfully() {
        Integer roleId = 1;
        role.setPermissions(Set.of(permission1));

        when(roleRepo.findById(roleId)).thenReturn(Optional.of(role));

        RoleResponse response = roleService.getRoleById(roleId);

        assertNotNull(response);
        assertEquals(1, response.id());
        assertEquals("ADMIN", response.name());
        assertEquals("System administrator", response.description());
        assertTrue(response.permissions().contains("USER_READ"));

        verify(roleRepo, times(1)).findById(roleId);
    }

    @Test
    void shouldThrowExceptionWhenRoleNotFoundById() {
        Integer roleId = 99;

        when(roleRepo.findById(roleId)).thenReturn(Optional.empty());

        RoleNotFoundException exception = assertThrows(
                RoleNotFoundException.class,
                () -> roleService.getRoleById(roleId)
        );

        assertEquals("Role not found with id: 99", exception.getMessage());

        verify(roleRepo, times(1)).findById(roleId);
    }

    @Test
    void shouldAssignPermissionsToRoleSuccessfully() {
        Integer roleId = 1;

        AssignOrRemovePermissionRequest request = new AssignOrRemovePermissionRequest(Set.of(1, 2));

        when(roleRepo.findById(roleId)).thenReturn(Optional.of(role));
        when(permissionRepo.findAllById(request.permissionIds()))
                .thenReturn(List.of(permission1, permission2));

        Role savedRole = new Role();
        savedRole.setId(1);
        savedRole.setName("ADMIN");
        savedRole.setDescription("System administrator");
        savedRole.setPermissions(Set.of(permission1, permission2));

        when(roleRepo.save(any(Role.class))).thenReturn(savedRole);

        RoleResponse response = roleService.assignPermissionsToRole(roleId, request);

        assertNotNull(response);
        assertEquals("ADMIN", response.name());
        assertEquals(2, response.permissions().size());
        assertTrue(response.permissions().contains("USER_READ"));
        assertTrue(response.permissions().contains("USER_CREATE"));

        verify(roleRepo, times(1)).findById(roleId);
        verify(permissionRepo, times(1)).findAllById(request.permissionIds());
        verify(roleRepo, times(1)).save(any(Role.class));
    }

    @Test
    void shouldThrowExceptionWhenAssigningPermissionsAndSomePermissionsNotFound() {
        Integer roleId = 1;

        AssignOrRemovePermissionRequest request = new AssignOrRemovePermissionRequest(Set.of(1, 2));

        when(roleRepo.findById(roleId)).thenReturn(Optional.of(role));
        when(permissionRepo.findAllById(request.permissionIds()))
                .thenReturn(List.of(permission1));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> roleService.assignPermissionsToRole(roleId, request)
        );

        assertEquals("One or more permissions not found", exception.getMessage());

        verify(roleRepo, times(1)).findById(roleId);
        verify(permissionRepo, times(1)).findAllById(request.permissionIds());
        verify(roleRepo, never()).save(any(Role.class));
    }

    @Test
    void shouldAddPermissionsToRoleSuccessfully() {
        Integer roleId = 1;

        role.setPermissions(new HashSet<>(Set.of(permission1)));

        AssignOrRemovePermissionRequest request = new AssignOrRemovePermissionRequest(Set.of(2));

        when(roleRepo.findById(roleId)).thenReturn(Optional.of(role));
        when(permissionRepo.findAllById(request.permissionIds()))
                .thenReturn(List.of(permission2));

        Role savedRole = new Role();
        savedRole.setId(1);
        savedRole.setName("ADMIN");
        savedRole.setDescription("System administrator");
        savedRole.setPermissions(Set.of(permission1, permission2));

        when(roleRepo.save(any(Role.class))).thenReturn(savedRole);

        RoleResponse response = roleService.addPermissionsToRole(roleId, request);

        assertNotNull(response);
        assertEquals("ADMIN", response.name());
        assertEquals(2, response.permissions().size());
        assertTrue(response.permissions().contains("USER_READ"));
        assertTrue(response.permissions().contains("USER_CREATE"));

        verify(roleRepo, times(1)).findById(roleId);
        verify(permissionRepo, times(1)).findAllById(request.permissionIds());
        verify(roleRepo, times(1)).save(any(Role.class));
    }

    @Test
    void shouldDeleteRoleSuccessfully() {
        Integer roleId = 1;

        when(roleRepo.findById(roleId)).thenReturn(Optional.of(role));
        doNothing().when(roleRepo).delete(role);

        roleService.hardDeleteRole(roleId);

        verify(roleRepo, times(1)).findById(roleId);
        verify(roleRepo, times(1)).delete(role);
    }

    @Test
    void shouldThrowExceptionWhenDeletingRoleNotFound() {
        Integer roleId = 99;

        when(roleRepo.findById(roleId)).thenReturn(Optional.empty());

        RoleNotFoundException exception = assertThrows(
                RoleNotFoundException.class,
                () -> roleService.hardDeleteRole(roleId)
        );

        assertEquals("Role not found with id: 99", exception.getMessage());

        verify(roleRepo, times(1)).findById(roleId);
        verify(roleRepo, never()).delete(any(Role.class));
    }
}
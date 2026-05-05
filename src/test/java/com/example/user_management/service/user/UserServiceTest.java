//package com.example.user_management.service.user;
//
//import com.example.user_management.exceptions.UserNotFoundException;
//import com.example.user_management.model.dto.UpdateUserRequest;
//import com.example.user_management.model.dto.UserRegisterRequest;
//import com.example.user_management.model.dto.UserResponse;
//import com.example.user_management.model.user.User;
//import com.example.user_management.repo.UserRepo;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class UserServiceTest {
//
//    @Mock
//    private UserRepo userRepo;
//
//    private UserService userService;
//
//    private User user;
//
//    @BeforeEach
//    void setUp() {
//        userService = new UserService(userRepo);
//
//        user = User.builder()
//                .id(1)
//                .username("ragu")
//                .email("ragu@gmail.com")
//                .password("encoded-password")
//                .active(true)
//                .deleted(false)
//                .build();
//    }
//
//    @Test
//    void shouldSaveUserSuccessfully() {
//        UserRegisterRequest request = new UserRegisterRequest(
//                "ragu",
//                "ragu@gmail.com",
//                "password123"
//        );
//
//        when(userRepo.save(any(User.class))).thenReturn(user);
//
//        UserResponse response = userService.saveUser(request);
//
//        assertNotNull(response);
//        assertEquals(1, response.id());
//        assertEquals("ragu", response.username());
//        assertEquals("ragu@gmail.com", response.email());
//        assertTrue(response.active());
//        assertFalse(response.deleted());
//
//        verify(userRepo, times(1)).save(any(User.class));
//    }
//
//    @Test
//    void shouldEncodePasswordWhenSavingUser() {
//        UserRegisterRequest request = new UserRegisterRequest(
//                "ragu",
//                "ragu@gmail.com",
//                "password123"
//        );
//
//        when(userRepo.save(any(User.class))).thenReturn(user);
//
//        userService.saveUser(request);
//
//        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
//        verify(userRepo).save(userCaptor.capture());
//
//        User savedUser = userCaptor.getValue();
//
//        assertEquals("ragu", savedUser.getUsername());
//        assertEquals("ragu@gmail.com", savedUser.getEmail());
//        assertNotEquals("password123", savedUser.getPassword());
//        assertTrue(savedUser.getPassword().startsWith("$2a$")
//                || savedUser.getPassword().startsWith("$2b$")
//                || savedUser.getPassword().startsWith("$2y$"));
//    }
//
//    @Test
//    void shouldGetUserByIdSuccessfully() {
//        Integer userId = 1;
//
//        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
//
//        UserResponse response = userService.getUserById(userId);
//
//        assertNotNull(response);
//        assertEquals(1, response.id());
//        assertEquals("ragu", response.username());
//        assertEquals("ragu@gmail.com", response.email());
//        assertTrue(response.active());
//        assertFalse(response.deleted());
//
//        verify(userRepo, times(1)).findById(userId);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenUserNotFoundById() {
//        Integer userId = 99;
//
//        when(userRepo.findById(userId)).thenReturn(Optional.empty());
//
//        UserNotFoundException exception = assertThrows(
//                UserNotFoundException.class,
//                () -> userService.getUserById(userId)
//        );
//
//        assertEquals("User not found with id: 99", exception.getMessage());
//
//        verify(userRepo, times(1)).findById(userId);
//    }
//
//    @Test
//    void shouldGetAllUsersWithoutDeletedUsers() {
//        User deletedUser = User.builder()
//                .id(2)
//                .username("deletedUser")
//                .email("deleted@gmail.com")
//                .password("encoded-password")
//                .active(false)
//                .deleted(true)
//                .build();
//
//        when(userRepo.findAll()).thenReturn(List.of(user, deletedUser));
//
//        List<UserResponse> responses = userService.getAllUsers();
//
//        assertEquals(1, responses.size());
//        assertEquals("ragu", responses.get(0).username());
//        assertFalse(responses.get(0).deleted());
//
//        verify(userRepo, times(1)).findAll();
//    }
//
//    @Test
//    void shouldUpdateUserSuccessfully() {
//        Integer userId = 1;
//
//        UpdateUserRequest request = new UpdateUserRequest(
//                "newRagu",
//                "newragu@gmail.com"
//        );
//
//        User updatedUser = User.builder()
//                .id(1)
//                .username("newRagu")
//                .email("newragu@gmail.com")
//                .password("encoded-password")
//                .active(true)
//                .deleted(false)
//                .build();
//
//        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
//        when(userRepo.save(any(User.class))).thenReturn(updatedUser);
//
//        UserResponse response = userService.updateUser(userId, request);
//
//        assertNotNull(response);
//        assertEquals("newRagu", response.username());
//        assertEquals("newragu@gmail.com", response.email());
//
//        verify(userRepo, times(1)).findById(userId);
//        verify(userRepo, times(1)).save(any(User.class));
//    }
//
//    @Test
//    void shouldUpdateOnlyUsernameWhenEmailIsNull() {
//        Integer userId = 1;
//
//        UpdateUserRequest request = new UpdateUserRequest(
//                "newRagu",
//                null
//        );
//
//        User updatedUser = User.builder()
//                .id(1)
//                .username("newRagu")
//                .email("ragu@gmail.com")
//                .password("encoded-password")
//                .active(true)
//                .deleted(false)
//                .build();
//
//        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
//        when(userRepo.save(any(User.class))).thenReturn(updatedUser);
//
//        UserResponse response = userService.updateUser(userId, request);
//
//        assertEquals("newRagu", response.username());
//        assertEquals("ragu@gmail.com", response.email());
//
//        verify(userRepo, times(1)).save(any(User.class));
//    }
//
//    @Test
//    void shouldUpdateOnlyEmailWhenUsernameIsNull() {
//        Integer userId = 1;
//
//        UpdateUserRequest request = new UpdateUserRequest(
//                null,
//                "newragu@gmail.com"
//        );
//
//        User updatedUser = User.builder()
//                .id(1)
//                .username("ragu")
//                .email("newragu@gmail.com")
//                .password("encoded-password")
//                .active(true)
//                .deleted(false)
//                .build();
//
//        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
//        when(userRepo.save(any(User.class))).thenReturn(updatedUser);
//
//        UserResponse response = userService.updateUser(userId, request);
//
//        assertEquals("ragu", response.username());
//        assertEquals("newragu@gmail.com", response.email());
//
//        verify(userRepo, times(1)).save(any(User.class));
//    }
//
//    @Test
//    void shouldDeactivateUserSuccessfully() {
//        Integer userId = 1;
//
//        User deactivatedUser = User.builder()
//                .id(1)
//                .username("ragu")
//                .email("ragu@gmail.com")
//                .password("encoded-password")
//                .active(false)
//                .deleted(false)
//                .build();
//
//        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
//        when(userRepo.save(any(User.class))).thenReturn(deactivatedUser);
//
//        UserResponse response = userService.deactivateUser(userId);
//
//        assertFalse(response.active());
//        assertFalse(response.deleted());
//
//        verify(userRepo, times(1)).findById(userId);
//        verify(userRepo, times(1)).save(any(User.class));
//    }
//
//    @Test
//    void shouldActivateUserSuccessfully() {
//        Integer userId = 1;
//
//        user.setActive(false);
//
//        User activatedUser = User.builder()
//                .id(1)
//                .username("ragu")
//                .email("ragu@gmail.com")
//                .password("encoded-password")
//                .active(true)
//                .deleted(false)
//                .build();
//
//        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
//        when(userRepo.save(any(User.class))).thenReturn(activatedUser);
//
//        UserResponse response = userService.activateUser(userId);
//
//        assertTrue(response.active());
//        assertFalse(response.deleted());
//
//        verify(userRepo, times(1)).findById(userId);
//        verify(userRepo, times(1)).save(any(User.class));
//    }
//
//    @Test
//    void shouldSoftDeleteUserSuccessfully() {
//        Integer userId = 1;
//
//        User deletedUser = User.builder()
//                .id(1)
//                .username("ragu")
//                .email("ragu@gmail.com")
//                .password("encoded-password")
//                .active(false)
//                .deleted(true)
//                .build();
//
//        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
//        when(userRepo.save(any(User.class))).thenReturn(deletedUser);
//
//        UserResponse response = userService.softDeleteUser(userId);
//
//        assertTrue(response.deleted());
//        assertFalse(response.active());
//
//        verify(userRepo, times(1)).findById(userId);
//        verify(userRepo, times(1)).save(any(User.class));
//    }
//
//    @Test
//    void shouldHardDeleteUserSuccessfully() {
//        Integer userId = 1;
//
//        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
//        doNothing().when(userRepo).delete(user);
//
//        Void response = userService.hardDeleteUser(userId);
//
//        assertNull(response);
//
//        verify(userRepo, times(1)).findById(userId);
//        verify(userRepo, times(1)).delete(user);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenHardDeleteUserNotFound() {
//        Integer userId = 99;
//
//        when(userRepo.findById(userId)).thenReturn(Optional.empty());
//
//        UserNotFoundException exception = assertThrows(
//                UserNotFoundException.class,
//                () -> userService.hardDeleteUser(userId)
//        );
//
//        assertEquals("User not found with id: 99", exception.getMessage());
//
//        verify(userRepo, times(1)).findById(userId);
//        verify(userRepo, never()).delete(any(User.class));
//    }
//}
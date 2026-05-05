package com.example.user_management.service.auth;

import com.example.user_management.entity.user.User;
import com.example.user_management.entity.user.UserPrincipal;
import com.example.user_management.repo.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {

    @Mock
    private UserRepo repo;

    private MyUserDetailsService myUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        myUserDetailsService = new MyUserDetailsService(repo);

        user = new User();
        user.setUsername("ragu");
        user.setPassword("password123");
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        String username = "ragu";

        when(repo.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails result = myUserDetailsService.loadUserByUsername(username);

        assertNotNull(result);
        assertInstanceOf(UserPrincipal.class, result);
        assertEquals(username, result.getUsername());

        verify(repo, times(1)).findByUsername(username);
    }

    @Test
    void shouldThrowUsernameNotFoundExceptionWhenUserNotFound() {
        String username = "wrongUser";

        when(repo.findByUsername(username)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> myUserDetailsService.loadUserByUsername(username)
        );

        assertEquals("User not found with username: " + username, exception.getMessage());

        verify(repo, times(1)).findByUsername(username);
    }
}
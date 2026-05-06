package com.example.user_management.service.auth;

import com.example.user_management.entity.user.User;
import com.example.user_management.entity.user.UserPrincipal;
import com.example.user_management.repo.UserRepo;
import com.example.user_management.service.impl.MyUserDetailsServiceImpl;
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

    private MyUserDetailsServiceImpl service;

    private User user;

    @BeforeEach
    void setUp() {
        service = new MyUserDetailsServiceImpl(repo);

        user = new User();
        user.setUsername("ragu"); // optional (not used anymore in auth)
        user.setEmail("ragu@gmail.com"); // IMPORTANT
        user.setPassword("encodedPassword");
        user.setActive(true);
        user.setDeleted(false);
    }

    @Test
    void shouldLoadUserByEmailSuccessfully() {

        String email = "ragu@gmail.com";

        when(repo.findByEmail(email)).thenReturn(Optional.of(user));

        UserDetails result = service.loadUserByUsername(email);

        assertNotNull(result);
        assertInstanceOf(UserPrincipal.class, result);
        assertEquals(email, result.getUsername()); // because getUsername() returns email

        verify(repo, times(1)).findByEmail(email);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        String email = "wrong@gmail.com";

        when(repo.findByEmail(email)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> service.loadUserByUsername(email)
        );

        assertEquals("User not found with email: " + email, exception.getMessage());

        verify(repo, times(1)).findByEmail(email);
    }
}
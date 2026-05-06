package com.example.user_management.service.auth;

import com.example.user_management.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private final String secret = "TmV3U2VjcmV0S2V5Rm9ySldUU2lnbmluZ1B1cnBvc2VzMTIzNDU2Nzg=";

    @BeforeEach
    void setUp() {
        jwtService = new JwtServiceImpl(secret);
    }

    @Test
    void shouldGenerateToken() {
        String username = "ragu@gmail.com";

        String token = jwtService.generateToken(username);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void shouldExtractUsernameFromToken() {
        String username = "ragu@gmail.com";

        String token = jwtService.generateToken(username);

        String extractedUsername = jwtService.extractUserName(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void shouldValidateTokenSuccessfully() {
        String username = "ragu@gmail.com";

        String token = jwtService.generateToken(username);

        UserDetails userDetails = new User(
                username,
                "password",
                Collections.emptyList()
        );

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertTrue(isValid);
    }

    @Test
    void shouldReturnFalseWhenUsernameDoesNotMatch() {
        String token = jwtService.generateToken("ragu@gmail.com");

        UserDetails userDetails = new User(
                "wrong@gmail.com",
                "password",
                Collections.emptyList()
        );

        boolean isValid = jwtService.validateToken(token, userDetails);

        assertFalse(isValid);
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {
        String invalidToken = "invalid.jwt.token";

        assertThrows(Exception.class, () -> {
            jwtService.extractUserName(invalidToken);
        });
    }
}
package com.example.user_management.service.auth;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(String email);
    String extractUserName(String token);
    boolean validateToken(String token, UserDetails userDetails);

}

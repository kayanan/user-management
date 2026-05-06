package com.example.user_management.controller;

import com.example.user_management.dto.request.LoginRequest;
import com.example.user_management.dto.request.UserRegisterRequest;
import com.example.user_management.dto.response.UserResponse;
import com.example.user_management.service.auth.JwtService;
import com.example.user_management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserAuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        return userService.saveUser(userRegisterRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
            String token = jwtService.generateToken(loginRequest.email());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}

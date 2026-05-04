package com.example.user_management.controller;

import com.example.user_management.model.User;
import com.example.user_management.model.dto.LoginRequest;
import com.example.user_management.model.dto.UserRegisterRequest;
import com.example.user_management.model.dto.UserResponse;
import com.example.user_management.service.UserService;
import com.example.user_management.service.auth.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public UserResponse register(@RequestBody UserRegisterRequest userRegisterRequest) {
        return userService.saveUser(userRegisterRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));
            String token = jwtService.generateToken(loginRequest.username());
            Cookie cookie = new Cookie("TOKEN", token);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60);
            cookie.setHttpOnly(false);
            cookie.setSecure(false);
            response.addCookie(cookie);
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }

    }

}

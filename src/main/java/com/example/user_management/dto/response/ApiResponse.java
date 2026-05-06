package com.example.user_management.dto.response;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {}
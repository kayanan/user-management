package com.example.user_management.utils;

import com.example.user_management.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    // Success (200 OK)
    public static <T> ResponseEntity<ApiResponse<T>> success(String message, T data) {
        return ResponseEntity.ok(new ApiResponse<>(true, message, data));
    }

    // Success with custom status
    public static <T> ResponseEntity<ApiResponse<T>> success(HttpStatus status, String message, T data) {
        return new ResponseEntity<>(new ApiResponse<>(true, message, data), status);
    }

    // Failure
    public static <T> ResponseEntity<ApiResponse<T>> failure(HttpStatus status, String message) {
        return new ResponseEntity<>(new ApiResponse<>(false, message, null), status);
    }
}

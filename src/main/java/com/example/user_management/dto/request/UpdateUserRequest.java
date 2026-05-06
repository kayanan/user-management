package com.example.user_management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
        String username,

        @Email(message = "Invalid email format")
        String email
) {
}

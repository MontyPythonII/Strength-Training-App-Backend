package com.workout.backend.dto.auth;

import com.workout.backend.model.users.UserType;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterUserRequest
(
    @Email @NotBlank String email,
    @NotBlank @Size(max = 100) String displayName,
    @NotNull UserType userType,
    @NotBlank @Size(min = 8, max = 128) String password
)
{}

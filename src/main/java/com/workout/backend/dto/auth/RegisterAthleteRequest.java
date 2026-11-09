package com.workout.backend.dto.auth;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterAthleteRequest
(
    @NotNull UUID inviteCode,
    @Email @NotBlank String email,
    @NotBlank @Size(max = 100) String displayName,
    @NotBlank @Size(min = 8, max = 128) String password
)
{}

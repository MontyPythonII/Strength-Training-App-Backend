package com.workout.backend.dto.athleteGroup;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AthleteGroupRequest
(
    @NotBlank @Size(max = 100) String name,
    @NotBlank @Size(max = 500) String description,
    Long scheduleId
)
{}

package com.workout.backend.dto.routine;

import java.util.List;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RoutineRequest
(
    @NotBlank @Size(max = 100) String name,
    @NotBlank @Size(max = 500) String description,
    @NotNull @UniqueElements List<@NotNull Long> exerciseIds
)
{}

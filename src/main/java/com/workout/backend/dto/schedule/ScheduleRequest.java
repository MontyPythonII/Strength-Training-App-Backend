package com.workout.backend.dto.schedule;

import java.time.DayOfWeek;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ScheduleRequest
(
    @NotBlank @Size(max = 100) String name,
    @NotBlank @Size(max = 500) String description,
    @NotNull Map<@NotNull DayOfWeek, @NotNull Long> routineIdsByDay
)
{}

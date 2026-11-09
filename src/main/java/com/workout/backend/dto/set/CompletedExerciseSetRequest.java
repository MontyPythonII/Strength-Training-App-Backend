package com.workout.backend.dto.set;

import java.math.BigDecimal;

import com.workout.backend.model.exercises.CompletedExerciseSet;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CompletedExerciseSetRequest
(
    @DecimalMin("0.0") @NotNull BigDecimal weight,
    @Min(1) @Max(200) Integer reps,
    @Min(1) Integer distance,
    @Min(1) Integer timeSeconds
)
{
    public CompletedExerciseSet toEntity(int setNumber)
    {
        return new CompletedExerciseSet(
            setNumber,
            this.weight,
            this.reps,
            this.distance,
            this.timeSeconds
        );
    }
}

package com.workout.backend.dto.set;

import java.math.BigDecimal;

import com.workout.backend.model.exercises.ExerciseSet;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record ExerciseSetRequest
(
    @Min(1) int setNumber,
    @DecimalMin("0.0") BigDecimal weight,
    @Min(1) @Max(200) Integer reps,
    @Min(1) Integer distance,
    @Min(1) Integer timeSeconds,
    @Size(max = 500) String notes
)
{
    public ExerciseSet toEntity()
    {
        ExerciseSet set = new ExerciseSet(
            this.setNumber,
            this.weight,
            this.reps,
            this.distance,
            this.timeSeconds,
            this.notes
        );
        return set;
    }
}

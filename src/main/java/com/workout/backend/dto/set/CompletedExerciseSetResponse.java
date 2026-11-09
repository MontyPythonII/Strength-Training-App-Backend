package com.workout.backend.dto.set;

import java.math.BigDecimal;

import com.workout.backend.model.exercises.CompletedExerciseSet;

public record CompletedExerciseSetResponse
(
    Long id,
    int setNumber,
    BigDecimal weight,
    Integer reps,
    Integer distance,
    Integer timeSeconds
)
{
    public static CompletedExerciseSetResponse from(CompletedExerciseSet set)
    {
        return new CompletedExerciseSetResponse(
            set.getId(),
            set.getSetNumber(),
            set.getWeight(),
            set.getReps(),
            set.getDistance(),
            set.getTimeSeconds()
        );
    }
}

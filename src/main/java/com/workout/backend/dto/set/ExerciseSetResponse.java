package com.workout.backend.dto.set;

import java.math.BigDecimal;

import com.workout.backend.model.exercises.ExerciseSet;

public record ExerciseSetResponse
(
    Long id,
    int setNumber,
    BigDecimal weight,
    Integer reps,
    Integer distance,
    Integer timeSeconds,
    String notes
)
{
    public static ExerciseSetResponse from(ExerciseSet set)
    {
        return new ExerciseSetResponse(
            set.getId(),
            set.getSetNumber(),
            set.getWeight(),
            set.getReps(),
            set.getDistance(),
            set.getTimeSeconds(),
            set.getNotes()
        );
    }
}

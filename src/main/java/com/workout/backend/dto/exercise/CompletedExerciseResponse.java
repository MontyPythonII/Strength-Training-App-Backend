package com.workout.backend.dto.exercise;

import java.util.List;

import com.workout.backend.dto.set.CompletedExerciseSetResponse;
import com.workout.backend.model.exercises.CompletedExercise;
import com.workout.backend.model.exercises.MeasureType;

public record CompletedExerciseResponse
(
    Long id,
    String name,
    String description,
    boolean barbellExercise,
    MeasureType measureType,
    List<CompletedExerciseSetResponse> sets
)
{
    public static CompletedExerciseResponse from(CompletedExercise exercise)
    {
        List<CompletedExerciseSetResponse> sets = exercise.getCompletedSets()
            .stream()
            .map(CompletedExerciseSetResponse::from)
            .toList();

        return new CompletedExerciseResponse(
            exercise.getId(),
            exercise.getName(),
            exercise.getDescription(),
            exercise.isBarbellExercise(),
            exercise.getMeasureType(),
            sets
        );
    }
}

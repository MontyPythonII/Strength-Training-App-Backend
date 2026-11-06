package com.workout.backend.dto.exercise;

import java.util.List;

import com.workout.backend.dto.set.ExerciseSetResponse;
import com.workout.backend.model.exercises.Exercise;
import com.workout.backend.model.exercises.MeasureType;

public record ExerciseResponse
(
    Long id,
    String name,
    String description,
    boolean barbellExercise,
    MeasureType measureType,
    List<ExerciseSetResponse> sets
)
{
    public static ExerciseResponse from(Exercise exercise)
    {
        List<ExerciseSetResponse> sets = exercise.getSets()
            .stream()
            .map(ExerciseSetResponse::from)
            .toList();

        return new ExerciseResponse(
            exercise.getId(),
            exercise.getName(),
            exercise.getDescription(),
            exercise.isBarbellExercise(),
            exercise.getMeasureType(),
            sets
        );
    }
}

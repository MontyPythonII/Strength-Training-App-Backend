package com.workout.backend.dto.routine;

import java.util.List;

import com.workout.backend.dto.exercise.ExerciseResponse;
import com.workout.backend.model.routines.Routine;

public record RoutineResponse
(
    Long id,
    String name,
    String description,
    List<ExerciseResponse> exercises
)
{
    public static RoutineResponse from(Routine routine)
    {
        List<ExerciseResponse> exercises = routine.getExercises()
            .stream()
            .map(ExerciseResponse::from)
            .toList();

        return new RoutineResponse(
            routine.getId(),
            routine.getName(),
            routine.getDescription(),
            exercises
        );
    }
}

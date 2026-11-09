package com.workout.backend.dto.routine;

import java.time.Instant;
import java.util.List;

import com.workout.backend.dto.exercise.CompletedExerciseResponse;
import com.workout.backend.model.routines.CompletedRoutine;

public record CompletedRoutineResponse
(
    Long id,
    String name,
    String description,
    Instant completedAt,
    List<CompletedExerciseResponse> exercises
)
{
    public static CompletedRoutineResponse from(CompletedRoutine routine)
    {
        List<CompletedExerciseResponse> exercises = routine.getCompletedExercises()
            .stream()
            .map(CompletedExerciseResponse::from)
            .toList();

        return new CompletedRoutineResponse(
            routine.getId(),
            routine.getName(),
            routine.getDescription(),
            routine.getCompletedAt(),
            exercises
        );
    }
}

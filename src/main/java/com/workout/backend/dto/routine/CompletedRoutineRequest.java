package com.workout.backend.dto.routine;

import java.util.List;

import com.workout.backend.dto.exercise.CompletedExerciseRequest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CompletedRoutineRequest
(
    @NotNull Long routineId,
    @NotEmpty List<@NotNull @Valid CompletedExerciseRequest> completedExercises
)
{
    
}

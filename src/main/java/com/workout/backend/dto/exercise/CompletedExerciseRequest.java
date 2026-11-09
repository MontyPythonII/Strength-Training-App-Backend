package com.workout.backend.dto.exercise;

import java.util.List;

import com.workout.backend.dto.set.CompletedExerciseSetRequest;
import com.workout.backend.model.exercises.CompletedExercise;
import com.workout.backend.model.exercises.Exercise;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CompletedExerciseRequest
(
    @NotNull Long exerciseId,
    @NotEmpty List<@NotNull @Valid CompletedExerciseSetRequest> completedSets
)
{
    public CompletedExercise toEntity(Exercise exercise)
    {
        CompletedExercise completedExercise = new CompletedExercise(exercise);

        for (int i = 0; i < this.completedSets.size(); i++)
        {
            completedExercise.addCompletedSet(this.completedSets.get(i).toEntity(i + 1));
        }

        return completedExercise;
    }

}

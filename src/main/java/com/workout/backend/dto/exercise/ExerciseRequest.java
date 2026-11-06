package com.workout.backend.dto.exercise;

import java.util.List;

import com.workout.backend.dto.set.ExerciseSetRequest;
import com.workout.backend.model.exercises.Exercise;
import com.workout.backend.model.exercises.MeasureType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ExerciseRequest
(
    @NotBlank @Size(max = 100) String name,
    @NotBlank @Size(max = 500) String description,
    boolean barbellExercise,
    @NotNull MeasureType measureType,
    @NotNull List<@Valid ExerciseSetRequest> sets
)
{
    public Exercise toEntity()
    {
        Exercise exercise = new Exercise(
            this.name,
            this.description,
            this.barbellExercise,
            this.measureType
        );
        this.sets.forEach(set -> exercise.addSet(set.toEntity()));
        return exercise;
    }

    public void applyTo(Exercise exercise)
    {
        exercise.setName(this.name);
        exercise.setDescription(this.description);
        exercise.setBarbellExercise(this.barbellExercise);
        exercise.setMeasureType(this.measureType);
        
        exercise.getSets().clear();
        this.sets.forEach(set -> exercise.addSet(set.toEntity()));
    }
}

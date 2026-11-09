package com.workout.backend.controller.coach;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.workout.backend.dto.exercise.ExerciseRequest;
import com.workout.backend.dto.exercise.ExerciseResponse;
import com.workout.backend.repository.ExerciseRepository;
import com.workout.backend.security.CoachOnly;
import com.workout.backend.security.CurrentUserId;
import com.workout.backend.service.ExerciseService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/coach/exercises")
@CoachOnly
public class CoachExerciseController
{
    //private final ExerciseRepository exerciseRepository;
    private final ExerciseService exerciseService;

    public CoachExerciseController(
        ExerciseRepository exerciseRepository,
        ExerciseService exerciseService
    )
    {
        //this.exerciseRepository = exerciseRepository;
        this.exerciseService = exerciseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExerciseResponse createExercise(
        @RequestBody @Valid ExerciseRequest request,
        @CurrentUserId Long userId
    )
    {
        return this.exerciseService.create(userId, request);
    }

    @GetMapping("/{id}")
    public ExerciseResponse getExercise(
        @PathVariable long id,
        @CurrentUserId Long userId
    )
    {
        return this.exerciseService.get(userId, id);
    }

    @GetMapping
    public List<ExerciseResponse> listExercises(
        @CurrentUserId Long userId
    )
    {
        return this.exerciseService.get(userId);
    }
}

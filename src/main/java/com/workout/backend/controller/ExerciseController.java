package com.workout.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.workout.backend.dto.exercise.ExerciseRequest;
import com.workout.backend.dto.exercise.ExerciseResponse;
//import com.workout.backend.model.exercises.Exercise;
import com.workout.backend.repository.ExerciseRepository;
import com.workout.backend.service.ExerciseService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/exercises")
public class ExerciseController
{
    //private final ExerciseRepository exerciseRepository;
    private final ExerciseService exerciseService;

    public ExerciseController(
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
        @AuthenticationPrincipal Jwt jwt
    )
    {
        return this.exerciseService.create(this.getCurrentUserId(jwt), request);
    }

    @GetMapping("/{id}")
    public ExerciseResponse getExercise(
        @PathVariable long id,
        @AuthenticationPrincipal Jwt jwt
    )
    {
        return this.exerciseService.get(this.getCurrentUserId(jwt), id);
    }

    @GetMapping
    public List<ExerciseResponse> listExercises(
        @AuthenticationPrincipal Jwt jwt
    )
    {
        return this.exerciseService.get(this.getCurrentUserId(jwt));
    }

    private Long getCurrentUserId(Jwt jwt)
    {
        return Long.valueOf(jwt.getSubject());
    }
}

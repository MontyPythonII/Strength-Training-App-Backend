package com.workout.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.workout.backend.dto.exercise.ExerciseRequest;
import com.workout.backend.dto.exercise.ExerciseResponse;
import com.workout.backend.model.exercises.Exercise;
import com.workout.backend.model.users.User;
import com.workout.backend.repository.ExerciseRepository;
import com.workout.backend.repository.UserRepository;
import com.workout.backend.service.exceptions.ExerciseNotFoundException;

import org.springframework.transaction.annotation.Transactional;

@Service
public class ExerciseService
{
    public final UserRepository userRepository;
    public final ExerciseRepository exerciseRepository;

    public ExerciseService(
        UserRepository userRepository,
        ExerciseRepository exerciseRepository
    )
    {
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Transactional
    public ExerciseResponse create(
        Long ownerId,
        ExerciseRequest request
    )
    {
        User owner = this.userRepository.getReferenceById(ownerId);

        Exercise exercise = request.toEntity();
        exercise.setOwner(owner);

        return ExerciseResponse.from(this.exerciseRepository.save(exercise));
    }

    @Transactional(readOnly = true)
    public ExerciseResponse get(
        Long ownerId,
        Long exerciseId
    )
    {
        Exercise exercise = this.exerciseRepository.findByIdAndOwnerId(exerciseId, ownerId)
            .orElseThrow(() -> new ExerciseNotFoundException(exerciseId));
        
        return ExerciseResponse.from(exercise);
    }

    @Transactional(readOnly = true)
    public List<ExerciseResponse> get(
        Long ownerId
    )
    {
        List<Exercise> exercises = this.exerciseRepository.findByOwnerId(ownerId);

        return exercises
            .stream()
            .map(ExerciseResponse::from)
            .toList();
    }
}

package com.workout.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workout.backend.dto.routine.RoutineRequest;
import com.workout.backend.dto.routine.RoutineResponse;
import com.workout.backend.model.exercises.Exercise;
import com.workout.backend.model.routines.Routine;
import com.workout.backend.model.users.User;
import com.workout.backend.repository.ExerciseRepository;
import com.workout.backend.repository.RoutineRepository;
import com.workout.backend.repository.UserRepository;
import com.workout.backend.service.exceptions.ModelNotFoundException;

@Service 
public class RoutineService
{
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final RoutineRepository routineRepository;

    public RoutineService(
        UserRepository userRepository,
        ExerciseRepository exerciseRepository,
        RoutineRepository routineRepository
    )
    {
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
        this.routineRepository = routineRepository;
    }

    @Transactional
    public RoutineResponse create(
        Long ownerId,
        RoutineRequest request
    )
    {
        User owner = this.userRepository.getReferenceById(ownerId);

        Routine routine = new Routine(
            request.name(),
            request.description()
        );
        routine.setOwner(owner);

        for (Exercise exercise : this.resolveExercisesInOrder(ownerId, request.exerciseIds()))
        {
            routine.addExercise(exercise);
        }

        return RoutineResponse.from(this.routineRepository.save(routine));
    }

    @Transactional
    public List<RoutineResponse> get(
        Long ownerId
    )
    {
        List<Routine> routines = this.routineRepository.findByOwnerId(ownerId);

        return routines
            .stream()
            .map(RoutineResponse::from)
            .toList();
    }

    @Transactional
    public RoutineResponse get(
        Long routineId,
        Long ownerId
    )
    {
        Routine routine = this.routineRepository.findByIdAndOwnerId(routineId, ownerId)
            .orElseThrow(() -> new ModelNotFoundException(Routine.class, routineId));
        
        return RoutineResponse.from(routine);
    }

    private List<Exercise> resolveExercisesInOrder(
        Long ownerId,
        List<Long> exerciseIds
    )
    {
        Map<Long, Exercise> byExercise = this.exerciseRepository
            .findByOwnerIdAndIdIn(ownerId, exerciseIds)
            .stream()
            .collect(Collectors.toMap(Exercise::getId, Function.identity()));
        
        List<Exercise> ordered = new ArrayList<>();

        for (Long id : exerciseIds)
        {
            Exercise exercise = byExercise.get(id);

            if (exercise == null) throw new ModelNotFoundException(Exercise.class, id);

            ordered.add(exercise);
        }

        return ordered;
    }
}

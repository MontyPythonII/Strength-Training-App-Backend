package com.workout.backend.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workout.backend.dto.exercise.CompletedExerciseRequest;
import com.workout.backend.dto.routine.CompletedRoutineRequest;
import com.workout.backend.dto.routine.CompletedRoutineResponse;
import com.workout.backend.dto.routine.RoutineRequest;
import com.workout.backend.dto.routine.RoutineResponse;
import com.workout.backend.model.exercises.Exercise;
import com.workout.backend.model.groups.AthleteGroup;
import com.workout.backend.model.routines.CompletedRoutine;
import com.workout.backend.model.routines.Routine;
import com.workout.backend.model.users.User;
import com.workout.backend.repository.CompletedRoutineRepository;
import com.workout.backend.repository.ExerciseRepository;
import com.workout.backend.repository.RoutineRepository;
import com.workout.backend.repository.UserRepository;
import com.workout.backend.service.exceptions.ModelNotFoundException;
import com.workout.backend.service.exceptions.NoActiveScheduleException;
import com.workout.backend.service.exceptions.NoRoutineForGivenDayException;
import com.workout.backend.service.exceptions.WrongRoutineForDayException;

@Service 
public class RoutineService
{
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final RoutineRepository routineRepository;
    private final CompletedRoutineRepository completedRoutineRepository;
    private final ScheduleService scheduleService;

    public RoutineService(
        UserRepository userRepository,
        ExerciseRepository exerciseRepository,
        RoutineRepository routineRepository,
        CompletedRoutineRepository completedRoutineRepository,
        ScheduleService scheduleService
    )
    {
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
        this.routineRepository = routineRepository;
        this.completedRoutineRepository = completedRoutineRepository;
        this.scheduleService = scheduleService;
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

    @Transactional
    public List<RoutineResponse> getByAthleteId(
        Long athleteId
    )
    {
        return this.scheduleService.getByAthleteId(athleteId).routinesByDay()
            .values()
            .stream()
            .distinct()
            .toList();
    }

    @Transactional
    public RoutineResponse getByAthleteId(
        DayOfWeek day,
        Long athleteId
    )
    {
        RoutineResponse routine = this.scheduleService.getByAthleteId(athleteId).routinesByDay().get(day);

        if (routine == null)
        {
            throw new NoRoutineForGivenDayException();
        }

        return routine;
    }

    @Transactional
    public CompletedRoutineResponse complete(
        long athleteId,
        CompletedRoutineRequest request,
        ZoneId timeZone
    )
    {
        User athlete = this.userRepository.getReferenceById(athleteId);

        AthleteGroup group = athlete.getAthleteGroup();

        if (group == null || group.getActiveSchedule() == null)
        {
            throw new NoActiveScheduleException();
        }

        Routine routine = group.getActiveSchedule().getRoutinesByDay().get(LocalDate.now(timeZone).getDayOfWeek());

        if (routine == null)
        {
            throw new NoRoutineForGivenDayException();
        }

        if (!routine.getId().equals(request.routineId()))
        {
            throw new WrongRoutineForDayException();
        }

        CompletedRoutine completedRoutine = new CompletedRoutine(athlete, routine);

        for (CompletedExerciseRequest exerciseRequest : request.completedExercises())
        {
            Exercise exercise = this.findRoutineExercise(routine, exerciseRequest.exerciseId());
            completedRoutine.addCompletedExercise(exerciseRequest.toEntity(exercise));
        }

        return (CompletedRoutineResponse.from(this.completedRoutineRepository.save(completedRoutine)));
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

    // Only exercises that are part of the routine can be logged against it
    private Exercise findRoutineExercise(Routine routine, Long exerciseId)
    {
        return routine.getExercises()
            .stream()
            .filter(exercise -> exercise.getId().equals(exerciseId))
            .findFirst()
            .orElseThrow(() -> new ModelNotFoundException(Exercise.class, exerciseId));
    }
}

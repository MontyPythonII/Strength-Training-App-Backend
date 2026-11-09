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

import com.workout.backend.dto.routine.RoutineRequest;
import com.workout.backend.dto.routine.RoutineResponse;
import com.workout.backend.security.CoachOnly;
import com.workout.backend.security.CurrentUserId;
import com.workout.backend.service.RoutineService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/coach/routines")
@CoachOnly
public class CoachRoutineController
{
    private final RoutineService routineService;

    public CoachRoutineController(
        RoutineService routineService
    )
    {
        this.routineService = routineService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoutineResponse createRoutine(
        @RequestBody @Valid RoutineRequest request,
        @CurrentUserId Long userId
    )
    {
        return this.routineService.create(userId, request);
    }

    @GetMapping("/{id}")
    public RoutineResponse getRoutine(
        @PathVariable Long id,
        @CurrentUserId Long userId
    )
    {
        return this.routineService.get(id, userId);
    }

    @GetMapping
    public List<RoutineResponse> listRoutines(
        @CurrentUserId Long userId
    )
    {
        return this.routineService.get(userId);
    }
}

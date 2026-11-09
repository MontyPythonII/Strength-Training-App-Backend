package com.workout.backend.controller.athlete;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.workout.backend.dto.routine.CompletedRoutineRequest;
import com.workout.backend.dto.routine.CompletedRoutineResponse;
import com.workout.backend.dto.routine.RoutineResponse;
import com.workout.backend.security.AthleteOnly;
import com.workout.backend.security.CurrentUserId;
import com.workout.backend.service.RoutineService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/athlete/routines")
@AthleteOnly 
public class AthleteRoutineController
{
    private final RoutineService routineService;

    public AthleteRoutineController(
        RoutineService routineService
    )
    {
        this.routineService = routineService;
    }

    @GetMapping
    public List<RoutineResponse> getRoutines(
        @CurrentUserId Long athleteId
    )
    {
        return this.routineService.getByAthleteId(athleteId);
    }

    @GetMapping("/{day}")
    public RoutineResponse getRoutine(
        @PathVariable DayOfWeek day,
        @CurrentUserId Long athleteId
    )
    {
        return this.routineService.getByAthleteId(day, athleteId);
    }

    @PostMapping("/complete{routineId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletedRoutineResponse completeRoutine(
        @RequestBody @Valid CompletedRoutineRequest request,
        // IANA zone like America/Chicago so "today" is the athlete's day, not the server's
        @RequestHeader("X-Time-Zone") ZoneId timZone,
        @CurrentUserId Long athleteId
    )
    {
        return this.routineService.complete(athleteId, request, timZone);
    }

}

package com.workout.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.workout.backend.dto.schedule.ScheduleRequest;
import com.workout.backend.dto.schedule.ScheduleResponse;
import com.workout.backend.security.CurrentUserId;
import com.workout.backend.service.ScheduleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController
{
    private final ScheduleService scheduleService;

    public ScheduleController(
        ScheduleService scheduleService
    )
    {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ScheduleResponse createSchedule(
        @RequestBody @Valid ScheduleRequest request,
        @CurrentUserId Long userId
    )
    {
        return this.scheduleService.create(userId, request);
    }

    @GetMapping("/{id}")
    public ScheduleResponse getSchedule(
        @PathVariable Long id,
        @CurrentUserId Long userId
    )
    {
        return this.scheduleService.get(id, userId);
    }

    @GetMapping
    public List<ScheduleResponse> listSchedules(
        @CurrentUserId Long userId
    )
    {
        return this.scheduleService.get(userId);
    }
}

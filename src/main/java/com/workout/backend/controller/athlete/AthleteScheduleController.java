package com.workout.backend.controller.athlete;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workout.backend.dto.schedule.ScheduleResponse;
import com.workout.backend.security.AthleteOnly;
import com.workout.backend.security.CurrentUserId;
import com.workout.backend.service.ScheduleService;

@RestController
@RequestMapping("/api/athlete/schedule")
@AthleteOnly
public class AthleteScheduleController
{
    private final ScheduleService scheduleService;

    public AthleteScheduleController(
        ScheduleService scheduleService
    )
    {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ScheduleResponse getSchedule(
        @CurrentUserId Long userId
    )
    {
        return this.scheduleService.getByAthleteId(userId);
    }
}

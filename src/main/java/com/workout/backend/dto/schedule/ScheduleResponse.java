package com.workout.backend.dto.schedule;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

import com.workout.backend.dto.routine.RoutineResponse;
import com.workout.backend.model.schedules.Schedule;

public record ScheduleResponse
(
    Long id,
    String name,
    String description,
    Map<DayOfWeek, RoutineResponse> routinesByDay
)
{
    public static ScheduleResponse from(Schedule schedule)
    {
        Map<DayOfWeek, RoutineResponse> routineResponses = new HashMap<>();

        schedule.getRoutinesByDay()
            .forEach((day, routine) -> routineResponses.put(day, RoutineResponse.from(routine)));
        
        return new ScheduleResponse(
            schedule.getId(),
            schedule.getName(),
            schedule.getDescription(),
            routineResponses
        );
    }
}

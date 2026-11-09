package com.workout.backend.dto.schedule;

import java.time.DayOfWeek;
import java.util.EnumMap;
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
        // EnumMap iterates in declaration order, so clients always get the days Monday through Sunday
        Map<DayOfWeek, RoutineResponse> routineResponses = new EnumMap<>(DayOfWeek.class);

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

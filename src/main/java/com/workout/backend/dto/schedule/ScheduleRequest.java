package com.workout.backend.dto.schedule;

import java.time.DayOfWeek;
import java.util.Map;

public record ScheduleRequest
(
    String name,
    String description,
    Map<DayOfWeek, Long> routineIdsByDay
)
{}

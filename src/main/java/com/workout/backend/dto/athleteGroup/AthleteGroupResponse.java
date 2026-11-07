package com.workout.backend.dto.athleteGroup;

import com.workout.backend.dto.schedule.ScheduleResponse;
import com.workout.backend.model.groups.AthleteGroup;

public record AthleteGroupResponse
(
    Long id,
    String name,
    String description,
    ScheduleResponse activeSchedule
)
{
    public static AthleteGroupResponse from(AthleteGroup group)
    {
        return new AthleteGroupResponse(
            group.getId(),
            group.getName(),
            group.getDescription(),
            group.getActiveSchedule() == null ? null : ScheduleResponse.from(group.getActiveSchedule())
        );
    }
}

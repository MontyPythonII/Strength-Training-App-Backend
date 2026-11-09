package com.workout.backend.dto.athleteGroup;

import java.util.UUID;

import com.workout.backend.dto.schedule.ScheduleResponse;
import com.workout.backend.model.groups.AthleteGroup;

public record AthleteGroupResponse
(
    Long id,
    UUID inviteCode,
    String name,
    String description,
    ScheduleResponse activeSchedule
)
{
    public static AthleteGroupResponse from(AthleteGroup group)
    {
        return new AthleteGroupResponse(
            group.getId(),
            group.getInviteCode(),
            group.getName(),
            group.getDescription(),
            group.getActiveSchedule() == null ? null : ScheduleResponse.from(group.getActiveSchedule())
        );
    }
}

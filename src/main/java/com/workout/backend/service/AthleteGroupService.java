package com.workout.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.workout.backend.dto.athleteGroup.AthleteGroupRequest;
import com.workout.backend.dto.athleteGroup.AthleteGroupResponse;
import com.workout.backend.model.groups.AthleteGroup;
import com.workout.backend.model.schedules.Schedule;
import com.workout.backend.model.users.User;
import com.workout.backend.repository.AthleteGroupRepository;
import com.workout.backend.repository.ScheduleRepository;
import com.workout.backend.repository.UserRepository;
import com.workout.backend.service.exceptions.ModelNotFoundException;

import org.springframework.transaction.annotation.Transactional;

@Service
public class AthleteGroupService
{
    private final UserRepository userRepository;
    private final AthleteGroupRepository athleteGroupRepository;
    private final ScheduleRepository scheduleRepository;

    public AthleteGroupService(
        UserRepository userRepository,
        AthleteGroupRepository athleteGroupRepository,
        ScheduleRepository scheduleRepository
    )
    {
        this.userRepository = userRepository;
        this.athleteGroupRepository = athleteGroupRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public AthleteGroupResponse create(
        Long ownerId,
        AthleteGroupRequest request
    )
    {
        User owner = this.userRepository.getReferenceById(ownerId);

        Schedule schedule = this.resolveSchedule(request.scheduleId(), ownerId);
        
        AthleteGroup athleteGroup = new AthleteGroup(
            owner,
            request.name(),
            request.description(),
            schedule
        );

        return AthleteGroupResponse.from(this.athleteGroupRepository.save(athleteGroup));
    }

    @Transactional
    public AthleteGroupResponse get(
        Long id,
        Long ownerId
    )
    {
        AthleteGroup athleteGroup = this.athleteGroupRepository.findByIdAndOwnerId(id, ownerId)
            .orElseThrow(() -> new ModelNotFoundException(AthleteGroup.class, id));
        
        return AthleteGroupResponse.from(athleteGroup);
    }

    @Transactional
    public List<AthleteGroupResponse> get(
        Long ownerId
    )
    {
        List<AthleteGroup> groups = this.athleteGroupRepository.findByOwnerId(ownerId);

        return groups
            .stream()
            .map(AthleteGroupResponse::from)
            .toList();
    }

    private Schedule resolveSchedule(
        Long scheduleId,
        Long ownerId
    )
    {
        if (scheduleId == null)
        {
            return null;
        }

        return this.scheduleRepository.findByIdAndOwnerId(scheduleId, ownerId)
            .orElseThrow(() -> new ModelNotFoundException(Schedule.class, scheduleId));
    }
}

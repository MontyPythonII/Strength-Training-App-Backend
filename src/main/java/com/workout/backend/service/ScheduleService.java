package com.workout.backend.service;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workout.backend.dto.schedule.ScheduleRequest;
import com.workout.backend.dto.schedule.ScheduleResponse;
import com.workout.backend.model.groups.AthleteGroup;
import com.workout.backend.model.routines.Routine;
import com.workout.backend.model.schedules.Schedule;
import com.workout.backend.model.users.User;
import com.workout.backend.repository.RoutineRepository;
import com.workout.backend.repository.ScheduleRepository;
import com.workout.backend.repository.UserRepository;
import com.workout.backend.service.exceptions.ModelNotFoundException;
import com.workout.backend.service.exceptions.NoActiveScheduleException;

@Service 
public class ScheduleService
{
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final RoutineRepository routineRepository;

    public ScheduleService(
        UserRepository userRepository,
        ScheduleRepository scheduleRepository,
        RoutineRepository routineRepository
    )
    {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.routineRepository = routineRepository;
    }

    @Transactional
    public ScheduleResponse create(
        Long ownerId,
        ScheduleRequest request
    )
    {
        User owner = this.userRepository.getReferenceById(ownerId);

        Map<DayOfWeek, Routine> routinesByDay = new HashMap<>();

        // Looked up by owner so a coach can't put another coach's routine on their schedule
        request.routineIdsByDay()
            .forEach((day, routineId) -> routinesByDay.put(day, this.routineRepository
                .findByIdAndOwnerId(routineId, ownerId)
                .orElseThrow(() -> new ModelNotFoundException(Routine.class, routineId))));

        Schedule schedule = new Schedule(
            owner,
            request.name(),
            request.description(),
            routinesByDay
        );
        
        return ScheduleResponse.from(this.scheduleRepository.save(schedule));
    }

    @Transactional
    public ScheduleResponse get(
        Long id,
        Long ownerId
    )
    {
        Schedule schedule = this.scheduleRepository.findByIdAndOwnerId(id, ownerId)
            .orElseThrow(() -> new ModelNotFoundException(Schedule.class, id));
        
        return ScheduleResponse.from(schedule);
    }

    @Transactional
    public List<ScheduleResponse> get(
        Long ownerId
    )
    {
        List<Schedule> schedule = this.scheduleRepository.findByOwnerId(ownerId);

        return schedule
            .stream()
            .map(ScheduleResponse::from)
            .toList();
    }

    @Transactional
    public ScheduleResponse getByAthleteId(
        Long athleteId
    )
    {
        User athlete = this.userRepository.getReferenceById(athleteId);

        AthleteGroup group = athlete.getAthleteGroup();

        if (group == null || group.getActiveSchedule() == null)
        {
            throw new NoActiveScheduleException();
        }

        return ScheduleResponse.from(group.getActiveSchedule());
    }

    @Transactional
    public Schedule getScheduleByAthleteId(
        Long athleteId
    )
    {
        User athlete = this.userRepository.getReferenceById(athleteId);

        AthleteGroup group = athlete.getAthleteGroup();

        if (group == null || group.getActiveSchedule() == null)
        {
            throw new NoActiveScheduleException();
        }

        return group.getActiveSchedule();
    }
}
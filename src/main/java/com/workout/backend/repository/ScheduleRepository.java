package com.workout.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workout.backend.model.schedules.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>
{
    Optional<Schedule> findByIdAndOwnerId(Long id, Long ownerId);

    List<Schedule> findByOwnerId(Long ownerId);
}

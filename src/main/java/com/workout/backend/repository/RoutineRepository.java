package com.workout.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workout.backend.model.routines.Routine;

public interface RoutineRepository extends JpaRepository<Routine, Long>
{
    Optional<Routine> findByIdAndOwnerId(Long id, Long ownerId);

    List<Routine> findByOwnerId(Long ownerId);
}

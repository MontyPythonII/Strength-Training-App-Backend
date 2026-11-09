package com.workout.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workout.backend.model.routines.CompletedRoutine;

public interface CompletedRoutineRepository extends JpaRepository<CompletedRoutine, Long>
{
    
}

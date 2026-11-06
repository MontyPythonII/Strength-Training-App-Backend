package com.workout.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workout.backend.model.exercises.ExerciseSet;

public interface ExerciseSetRepository extends JpaRepository<ExerciseSet, Long>
{
    List<ExerciseSet> findByExerciseId(Long id);
}

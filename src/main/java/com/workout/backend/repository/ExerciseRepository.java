package com.workout.backend.repository;

import com.workout.backend.model.exercises.Exercise;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long>
{
    Optional<Exercise> findByIdAndOwnerId(Long id, Long ownerId);

    List<Exercise> findByOwnerId(Long ownerId);


    List<Exercise> findByOwnerIdAndIdIn(Long ownerId, Collection<Long> ids);

}

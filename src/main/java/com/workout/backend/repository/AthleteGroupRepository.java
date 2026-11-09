package com.workout.backend.repository;

import com.workout.backend.model.groups.AthleteGroup;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AthleteGroupRepository extends JpaRepository<AthleteGroup, Long>
{
    Optional<AthleteGroup> findByIdAndOwnerId(Long id, Long ownerId);

    List<AthleteGroup> findByOwnerId(Long ownerId);

    Optional<AthleteGroup> findByInviteCode(UUID inviteCode);
}

package com.workout.backend.model.exercises;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "completed_exercise_set")
public class CompletedExerciseSet extends BaseExerciseSet
{
    @Column(nullable = false)
    private Instant completedAt = Instant.now();

    protected CompletedExerciseSet() {}

    public CompletedExerciseSet(int setNumber, BigDecimal weight, Integer reps, Integer distance, Integer timeSeconds)
    {
        super(setNumber, weight, reps, distance, timeSeconds);
    }

    @PrePersist
    private void stampCompletedAt()
    {
        if (this.completedAt == null) {this.completedAt = Instant.now();}
    }

    public Instant getCompletedAt()
    {
        return this.completedAt;
    }
}

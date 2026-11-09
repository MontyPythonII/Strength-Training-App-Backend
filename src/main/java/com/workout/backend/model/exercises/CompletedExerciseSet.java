package com.workout.backend.model.exercises;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

/**
 * A set an athlete actually performed, recorded against the logged exercise rather than the coach's template.
 */
@Entity
@Table(name = "completed_exercise_set")
public class CompletedExerciseSet extends BaseExerciseSet
{
    @Column(nullable = false)
    private Instant completedAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "completed_exercise_id", nullable = false)
    private CompletedExercise completedExercise;

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

    public CompletedExercise getCompletedExercise()
    {
        return this.completedExercise;
    }

    // Package-private so the link is only set through CompletedExercise.addSet, which keeps both sides in sync
    void setCompletedExercise(CompletedExercise completedExercise)
    {
        this.completedExercise = completedExercise;
    }
}

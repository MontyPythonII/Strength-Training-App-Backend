package com.workout.backend.model.exercises;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Entity
@Table(name = "exercise_set", uniqueConstraints = @UniqueConstraint(columnNames = {"exercise_id", "set_number"}))
public class ExerciseSet extends BaseExerciseSet
{
    @Column(length = 500)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    protected Exercise exercise;

    protected ExerciseSet() {}

    public ExerciseSet(
        int setNumber,
        BigDecimal weight,
        Integer reps,
        Integer distance,
        Integer timeSeconds,
        String notes
    )
    {
        super(setNumber, weight, reps, distance, timeSeconds);
        this.notes = notes;
    }

    public String getNotes()
    {
        return this.notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public Exercise getExercise()
    {
        return this.exercise;
    }

    public void setExercise(Exercise exercise)
    {
        this.exercise = exercise;
    }
}

package com.workout.backend.model.exercises;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Entity
@Table(name = "exercise_set", uniqueConstraints = @UniqueConstraint(columnNames = {"exercise_id", "set_number"}))
public class ExerciseSet extends BaseExerciseSet
{
    @Column(length = 500)
    private String notes;

    protected ExerciseSet() {}

    public ExerciseSet(int setNumber, BigDecimal weight, Integer reps, Integer distance, Integer timeSeconds)
    {
        super(setNumber, weight, reps, distance, timeSeconds);
    }

    public String getNotes()
    {
        return this.notes;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }
}

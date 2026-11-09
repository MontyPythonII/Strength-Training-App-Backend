package com.workout.backend.model.exercises;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@MappedSuperclass
public abstract class BaseExerciseSet
{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Min(1)
    @Column(nullable = false)
    protected int setNumber;

    // Always stored as lbs. Frontends can convert to kg when needed
    @DecimalMin("0.0")
    @Column(nullable = false, precision = 6, scale = 2)
    protected BigDecimal weight;

    @Min(1)
    @Max(200)
    protected Integer reps;

    // Always stored as feet. Frontends can convert to meters when needed
    @Min(1)
    protected Integer distance;

    @Min(1)
    protected Integer timeSeconds;

    protected BaseExerciseSet() {}

    public BaseExerciseSet(int setNumber, BigDecimal weight, Integer reps, Integer distance, Integer timeSeconds)
    {
        this.setNumber = setNumber;
        this.weight = weight;
        this.reps = reps;
        this.distance = distance;
        this.timeSeconds = timeSeconds;
    }

    public Long getId()
    {
        return this.id;
    }

    public int getSetNumber()
    {
        return this.setNumber;
    }

    public void setSetNumber(int setNumber)
    {
        this.setNumber = setNumber;
    }

    public BigDecimal getWeight()
    {
        return this.weight;
    }

    public void setWeight(BigDecimal weight)
    {
        this.weight = weight;
    }

    public Integer getReps()
    {
        return this.reps;
    }

    public void setReps(Integer reps)
    {
        this.reps = reps;
    }

    public Integer getDistance()
    {
        return this.distance;
    }

    public void setDistance(Integer distance)
    {
        this.distance = distance;
    }

    public Integer getTimeSeconds()
    {
        return this.timeSeconds;
    }

    public void setTimeSeconds(Integer timeSeconds)
    {
        this.timeSeconds = timeSeconds;
    }
}

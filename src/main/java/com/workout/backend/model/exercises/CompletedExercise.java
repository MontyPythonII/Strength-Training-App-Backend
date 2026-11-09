package com.workout.backend.model.exercises;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.workout.backend.model.routines.CompletedRoutine;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

/**
 * One exercise inside a logged workout. Name, description and measure type are copied from the template so history still reads correctly after the coach edits or deletes it.
 */
@Entity
@Table(name = "completed_exercise")
public class CompletedExercise
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    private boolean barbellExercise;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private MeasureType measureType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "completed_routine_id", nullable = false)
    private CompletedRoutine completedRoutine;

    // Nullable so deleting the coach's template keeps the athlete's history while still allowing progress to be charted per exercise
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;

    // A plain column rather than @OrderColumn, which on the mappedBy side inserts null first and fails the NOT NULL constraint
    @Column(nullable = false)
    private int position;

    @OneToMany(mappedBy = "completedExercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("completedAt ASC, setNumber ASC")
    private List<CompletedExerciseSet> completedSets = new ArrayList<>();

    protected CompletedExercise() {}

    public CompletedExercise(Exercise exercise)
    {
        this.exercise = exercise;
        this.name = exercise.getName();
        this.description = exercise.getDescription();
        this.barbellExercise = exercise.isBarbellExercise();
        this.measureType = exercise.getMeasureType();
    }

    public Long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDescription()
    {
        return this.description;
    }

    public boolean isBarbellExercise()
    {
        return this.barbellExercise;
    }

    public MeasureType getMeasureType()
    {
        return this.measureType;
    }

    public CompletedRoutine getCompletedRoutine()
    {
        return this.completedRoutine;
    }

    public Exercise getExercise()
    {
        return this.exercise;
    }

    public int getPosition()
    {
        return this.position;
    }

    public List<CompletedExerciseSet> getCompletedSets()
    {
        return Collections.unmodifiableList(this.completedSets);
    }

    public void addCompletedSet(CompletedExerciseSet completedSet)
    {
        this.completedSets.add(completedSet);
        completedSet.setCompletedExercise(this);
    }

    // Called by CompletedRoutine.addCompletedExercise so the routine link and order are always set together
    public void attachTo(CompletedRoutine completedRoutine, int position)
    {
        this.completedRoutine = completedRoutine;
        this.position = position;
    }
}

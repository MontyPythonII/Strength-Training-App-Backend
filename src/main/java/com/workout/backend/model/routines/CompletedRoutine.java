package com.workout.backend.model.routines;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.workout.backend.model.exercises.CompletedExercise;
import com.workout.backend.model.users.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * A workout an athlete logged. Name and description are copied from the routine so history survives the coach editing or deleting it.
 */
@Entity
@Table(name = "completed_routine", uniqueConstraints = @UniqueConstraint(columnNames = {"athlete_id", "completed_on"}))
public class CompletedRoutine
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "athlete_id", nullable = false)
    private User athlete;

    // Nullable so deleting the coach's routine keeps the athlete's history
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private Instant completedAt = Instant.now();

    // The calendar day the workout counts for, so the database can enforce one log per athlete per day
    @Column(nullable = false)
    private LocalDate completedOn;

    @OneToMany(mappedBy = "completedRoutine", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<CompletedExercise> completedExercises = new ArrayList<>();

    protected CompletedRoutine() {}

    public CompletedRoutine(User athlete, Routine routine, LocalDate completedOn)
    {
        this.athlete = athlete;
        this.routine = routine;
        this.name = routine.getName();
        this.description = routine.getDescription();
        this.completedOn = completedOn;
    }


    public Long getId()
    {
        return this.id;
    }

    public User getAthlete()
    {
        return this.athlete;
    }

    public Routine getRoutine()
    {
        return this.routine;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Instant getCompletedAt()
    {
        return this.completedAt;
    }

    public List<CompletedExercise> getCompletedExercises()
    {
        return Collections.unmodifiableList(this.completedExercises);
    }

    // Position comes from list order so the client never has to send it
    public void addCompletedExercise(CompletedExercise completedExercise)
    {
        completedExercise.attachTo(this, this.completedExercises.size());
        this.completedExercises.add(completedExercise);
    }
}

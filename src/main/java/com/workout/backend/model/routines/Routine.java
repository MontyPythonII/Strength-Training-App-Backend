package com.workout.backend.model.routines;

import java.util.ArrayList;
import java.util.List;

import com.workout.backend.model.exercises.Exercise;
import com.workout.backend.model.schedules.Schedule;
import com.workout.backend.model.users.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "routine")
public class Routine
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @ManyToMany
    @JoinTable(
        name = "routine_id",
        joinColumns = @JoinColumn(name = "routine_id"),
        inverseJoinColumns = @JoinColumn(name = "exercise_id")
    )
    @OrderColumn(name = "position")
    private List<Exercise> exercises = new ArrayList<>();

    @OneToMany(mappedBy = "schedule")
    private List<Schedule> schedules = new ArrayList<>();

    public Long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List<Exercise> getExercises()
    {
        return this.exercises;
    }

    public void addExercise(Exercise exercise)
    {
        this.exercises.add(exercise);
        exercise.addRoutine(this);
    }

    public List<Schedule> getSchedules()
    {
        return this.schedules;
    }

    public void addSchedule(Schedule schedule)
    {
        this.schedules.add(schedule);
    }
}

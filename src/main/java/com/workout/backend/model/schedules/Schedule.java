package com.workout.backend.model.schedules;

import com.workout.backend.model.groups.AthleteGroup;
import com.workout.backend.model.routines.Routine;
import com.workout.backend.model.users.User;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;

@Entity
@Table(name = "schedule")
public class Schedule
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

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private boolean hasEndDate;

    @OneToMany(mappedBy = "activeSchedule")
    private List<AthleteGroup> groups = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "schedule_routine",
        joinColumns = @JoinColumn(name = "schedule_id"),
        inverseJoinColumns = @JoinColumn(name = "routine_id")
    )
    @MapKeyColumn(name = "day_of_week", nullable = false, length = 9)
    @MapKeyEnumerated(EnumType.STRING)
    private Map<DayOfWeek, Routine> routinesByDay = new HashMap<>();

    protected Schedule() {}

    public Schedule(
        User owner,
        String name,
        String description,
        Map<DayOfWeek, Routine> routinesByDay
    )
    {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.routinesByDay = routinesByDay;
    }

    public Long getId()
    {
        return this.id;
    }

    public User getOwner()
    {
        return this.owner;
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

    public LocalDate getStartDate()
    {
        return this.startDate;
    }

    public void setStartDate(LocalDate date)
    {
        this.startDate = date;
    }

    public LocalDate getEndDate()
    {
        return this.endDate;
    }

    public void setEndDate(LocalDate date)
    {
        this.endDate = date;
    }

    public void assignRoutine(DayOfWeek day, Routine routine)
    {
        this.routinesByDay.put(day, routine);
    }

    public void clearDay(DayOfWeek day)
    {
        this.routinesByDay.remove(day);
    }

    public Optional<Routine> getRoutineFor(DayOfWeek day)
    {
        return Optional.ofNullable(this.routinesByDay.get(day));
    }

    public Map<DayOfWeek, Routine> getRoutinesByDay()
    {
        return Collections.unmodifiableMap(this.routinesByDay);
    }

    // Read-only because a group is attached through AthleteGroup.setActiveSchedule, which owns the foreign key
    public List<AthleteGroup> getGroups()
    {
        return Collections.unmodifiableList(this.groups);
    }

    public boolean getHasEndDate()
    {
        return this.hasEndDate;
    }

    public void setHasEndDate(boolean hasEndDate)
    {
        this.hasEndDate = hasEndDate;
    }
}

package com.workout.backend.model.exercises;

import java.util.ArrayList;
import java.util.List;

import com.workout.backend.model.routines.Routine;
import com.workout.backend.model.users.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;

@Entity
@Table(name = "exercise")
public class Exercise
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    private boolean barbellExercise;

    // Sized to fit DISTANCE, bump it if a longer measure type gets added
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 8)
    private MeasureType measureType;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("setNumber ASC")
    private List<ExerciseSet> sets = new ArrayList<>();

    @ManyToMany(mappedBy = "exercises")
    private List<Routine> routines = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;


    protected Exercise() {}

    public Exercise(
        String name,
        String description,
        boolean barbellExercise,
        MeasureType measureType
    )
    {
        this.name = name;
        this.description = description;
        this.barbellExercise = barbellExercise;
        this.measureType = measureType;
    }

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

    public List<ExerciseSet> getSets()
    {
        return this.sets;
    }

    public void setSets(List<ExerciseSet> sets)
    {
        this.sets = sets;
    }

    // Keeps both sides of the relationship in sync so Hibernate writes the foreign key.
    public void addSet(ExerciseSet set)
    {
        sets.add(set);
        set.setExercise(this);
    }

    public void removeSet(ExerciseSet set)
    {
        sets.remove(set);
        set.setExercise(null);
    }

    public void setBarbellExercise(boolean barbellExercise)
    {
        this.barbellExercise = barbellExercise;
    }

    public boolean isBarbellExercise()
    {
        return this.barbellExercise;
    }

    public MeasureType getMeasureType()
    {
        return this.measureType;
    }

    public void setMeasureType(MeasureType measureType)
    {
        this.measureType = measureType;
    }

    public void addRoutine(Routine routine)
    {
        this.routines.add(routine);
    }

    public List<Routine> getRoutines()
    {
        return this.routines;
    }

    public User getOwner()
    {
        return this.owner;
    }

    public void setOwner(User owner)
    {
        this.owner = owner;
    }
}

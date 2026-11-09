package com.workout.backend.model.groups;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.workout.backend.model.schedules.Schedule;
import com.workout.backend.model.users.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table (name = "athlete_group")
public class AthleteGroup
{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID inviteCode = UUID.randomUUID();

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "active_schedule_id")
    private Schedule activeSchedule;

    @OneToMany(mappedBy = "athleteGroup")
    private List<User> athletes = new ArrayList<>();

    protected AthleteGroup() {}

    public AthleteGroup(
        User owner,
        String name,
        String description,
        Schedule activeSchedule
    )
    {
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.activeSchedule = activeSchedule;
    }

    public Long getId()
    {
        return this.id;
    }

    public UUID getInviteCode()
    {
        return this.inviteCode;
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

    public User getOwner()
    {
        return this.owner;
    }

    public Schedule getActiveSchedule()
    {
        return this.activeSchedule;
    }

    public void setActiveSchedule(Schedule activeSchedule)
    {
        this.activeSchedule = activeSchedule;
    }

    public List<User> getAthletes()
    {
        return Collections.unmodifiableList(this.athletes);
    }

    // Membership is persisted through User.group, so the athlete's side has to change too or the save is a no-op
    public void addAthlete(User athlete)
    {
        this.athletes.add(athlete);
        athlete.setAthleteGroup(this);
    }

    public void removeAthlete(User athlete)
    {
        this.athletes.remove(athlete);
        athlete.setAthleteGroup(null);
    }
}

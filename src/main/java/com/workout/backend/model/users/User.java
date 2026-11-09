package com.workout.backend.model.users;

import java.util.ArrayList;
import java.util.List;

import com.workout.backend.model.exercises.Exercise;
import com.workout.backend.model.groups.AthleteGroup;
import com.workout.backend.model.routines.CompletedRoutine;
import com.workout.backend.model.routines.Routine;
import com.workout.backend.model.schedules.Schedule;

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
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String displayName;

    // 254 is the longest a valid email address can be
    @Column(nullable = false, unique = true, length = 254)
    private String email;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    // Sized to fit ATHLETE, bump it if a longer role gets added
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 7)
    private UserType userType;

    // Coaches and athletes share this table. These collections are only filled in for coaches.
    @OneToMany(mappedBy = "owner")
    private List<AthleteGroup> groups = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Schedule> schedules = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Routine> routines = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<Exercise> exercises = new ArrayList<>();

    @OneToMany(mappedBy = "coach")
    private List<User> athletes = new ArrayList<>();

    // Athlete-only fields
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "coach_id")
    private User coach;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "athlete_group_id")
    private AthleteGroup athleteGroup;

    @OneToMany(mappedBy = "athlete")
    private List<CompletedRoutine> completedRoutines = new ArrayList<>();


    protected User() {}

    private User(
        UserType userType,
        String email,
        String displayName,
        String passwordHash
    )
    {
        this.userType = userType;
        this.email = email;
        this.displayName = displayName;
        this.passwordHash = passwordHash;
    }

    public static User coach(
        String email,
        String displayName,
        String passwordHash
    )
    {
        return new User(UserType.COACH, email, displayName, passwordHash);
    }

    // An athlete's coach is whoever owns the group they joined through, so both are set together
    public static User athlete(
        AthleteGroup athleteGroup,
        String email,
        String displayName,
        String passwordHash
    )
    {
        User athlete = new User(UserType.ATHLETE, email, displayName, passwordHash);
        athlete.athleteGroup = athleteGroup;
        athlete.coach = athleteGroup.getOwner();

        return athlete;
    }

    public Long getId()
    {
        return id;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public UserType getUserType()
    {
        return userType;
    }

    public void setUserType(UserType userType)
    {
        this.userType = userType;
    }

    public List<Schedule> getSchedules()
    {
        return schedules;
    }

    public void addSchedule(Schedule schedule)
    {
        schedules.add(schedule);
    }

    public void removeSchedule(Schedule schedule)
    {
        schedules.remove(schedule);
    }

    public List<Routine> getRoutines()
    {
        return routines;
    }

    public List<Exercise> getExercises()
    {
        return exercises;
    }

    public List<CompletedRoutine> getCompletedRoutines()
    {
        return completedRoutines;
    }

    public List<AthleteGroup> getGroups()
    {
        return groups;
    }

    public void addGroup(AthleteGroup athleteGroup)
    {
        groups.add(athleteGroup);
    }

    public void removeGroup(AthleteGroup athleteGroup)
    {
        groups.remove(athleteGroup);
    }

    public List<User> getAthletes()
    {
        return athletes;
    }

    // Only coach_id is persisted, so the athlete's side has to change too or the save is a no-op
    public void addAthlete(User athlete)
    {
        athletes.add(athlete);
        athlete.setCoach(this);
    }

    public void removeAthlete(User athlete)
    {
        athletes.remove(athlete);
        athlete.setCoach(null);
    }

    public User getCoach()
    {
        return coach;
    }

    public void setCoach(User coach)
    {
        this.coach = coach;
    }

    public AthleteGroup getAthleteGroup()
    {
        return athleteGroup;
    }

    public void setAthleteGroup(AthleteGroup group)
    {
        this.athleteGroup = group;
    }

    public String getPasswordHash()
    {
        return this.passwordHash;
    }

    public void setPasswordHash(String passwordHash)
    {
        this.passwordHash = passwordHash;
    }
}

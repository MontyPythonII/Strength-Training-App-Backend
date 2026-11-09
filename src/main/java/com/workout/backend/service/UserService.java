package com.workout.backend.service;


import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.workout.backend.model.groups.AthleteGroup;
import com.workout.backend.model.users.User;
import com.workout.backend.repository.AthleteGroupRepository;
import com.workout.backend.repository.UserRepository;
import com.workout.backend.service.exceptions.EmailAlreadyRegisteredException;
import com.workout.backend.service.exceptions.InvalidInviteCodeException;

import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AthleteGroupRepository athleteGroupRepository;

    public UserService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        AthleteGroupRepository athleteGroupRepository
    )
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.athleteGroupRepository = athleteGroupRepository;
    }

    @Transactional
    public User registerCoach(
        String email,
        String displayName,
        String rawPassword
    )
    {
        this.ensureEmailAvailable(email);

        String hash = this.passwordEncoder.encode(rawPassword);

        return this.userRepository.save(User.coach(email, displayName, hash));
    }

    public boolean passwordMatched(User user, String rawPassword)
    {
        return this.passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }

    public User getByEmail(String email)
    {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalStateException("Authenticated user missing: " + email));
    }

    @Transactional
    public User registerAthlete(
        UUID code,
        String email,
        String displayName,
        String rawPassword
    )
    {
        AthleteGroup athleteGroup = this.athleteGroupRepository.findByInviteCode(code)
            .orElseThrow(() -> new InvalidInviteCodeException());

        this.ensureEmailAvailable(email);

        String hash = this.passwordEncoder.encode(rawPassword);

        return this.userRepository.save(User.athlete(athleteGroup, email, displayName, hash));
    }

    //@Transactional 

    private void ensureEmailAvailable(String email)
    {
        if (this.userRepository.existsByEmail(email))
        {
            throw new EmailAlreadyRegisteredException(email);
        }
    }
}

package com.workout.backend.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.workout.backend.model.users.User;
import com.workout.backend.model.users.UserType;
import com.workout.backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(String email, String displayName, UserType userType, String rawPassword)
    {
        if (this.userRepository.existsByEmail(email))
        {
            throw new IllegalStateException("Email already registered");
        }

        String hash = this.passwordEncoder.encode(rawPassword);

        return this.userRepository.save(new User(email, displayName, userType, hash));
    }

    public boolean passwordMatched(User user, String rawPassword)
    {
        return this.passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }
}

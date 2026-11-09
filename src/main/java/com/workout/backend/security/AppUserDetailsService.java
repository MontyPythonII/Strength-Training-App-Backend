package com.workout.backend.security;

import com.workout.backend.model.users.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.workout.backend.repository.UserRepository;

@Service
public class AppUserDetailsService implements UserDetailsService
{
    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        // Same message as a wrong password so login can't be used to check which emails have accounts
        User user = this.userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Bad credentials"));

        // Fully qualified because it clashes with our own User entity
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
            .password(user.getPasswordHash())
            .roles(user.getUserType().name())
            .build();
    }
}

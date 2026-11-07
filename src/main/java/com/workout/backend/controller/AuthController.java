package com.workout.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.workout.backend.dto.auth.LoginRequest;
import com.workout.backend.dto.auth.RegisterUserRequest;
import com.workout.backend.dto.auth.TokenResponse;
import com.workout.backend.dto.auth.UserResponse;
import com.workout.backend.model.users.User;
import com.workout.backend.service.TokenService;
import com.workout.backend.service.UserService;

import jakarta.validation.Valid;

/*
 * Public endpoints for creating an account and exchanging credentials for a token.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController
{
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, TokenService tokenService)
    {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(
        @Valid @RequestBody RegisterUserRequest request
    )
    {
        User user = this.userService.register(request.email(), request.displayName(), request.userType(), request.password());

        return UserResponse.from(user);
    }

    @PostMapping("/login")
    public TokenResponse login(
        @Valid @RequestBody LoginRequest request
    )
    {
        this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = this.userService.getByEmail(request.email());

        return new TokenResponse(this.tokenService.issueToken(user));
    }
}

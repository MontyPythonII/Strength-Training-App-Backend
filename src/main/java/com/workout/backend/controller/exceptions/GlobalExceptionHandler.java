package com.workout.backend.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.workout.backend.service.exceptions.EmailAlreadyRegisteredException;

/**
 * Translates exceptions escaping any controller into consistent ProblemDetail responses with the right HTTP status.
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    // Covers BadCredentialsException, DisabledException, LockedException and the rest of the AuthenticationException tree.
    // Message is fixed so the response never reveals whether the email exists.
    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthentication(AuthenticationException exception)
    {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Bad credentials");
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ProblemDetail handleDuplicateEmail(EmailAlreadyRegisteredException exception)
    {
        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, exception.getMessage());
    }
}


package com.workout.backend.service.exceptions;

/**
 * Registration attempted with an email that already has an account.
 */
public class EmailAlreadyRegisteredException extends RuntimeException
{
    public EmailAlreadyRegisteredException(String email)
    {
        super("Email already registered: " + email);
    }
}

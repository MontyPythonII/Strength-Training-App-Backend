package com.workout.backend.service.exceptions;


public class NoActiveScheduleException extends RuntimeException
{
    public NoActiveScheduleException()
    {
        super("Your group has no active schedule yet");
    }
}

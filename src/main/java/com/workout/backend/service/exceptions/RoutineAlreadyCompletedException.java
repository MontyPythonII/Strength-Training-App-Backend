package com.workout.backend.service.exceptions;

public class RoutineAlreadyCompletedException extends RuntimeException
{
    public RoutineAlreadyCompletedException()
    {
        super("This routine has already been completed");
    }
}

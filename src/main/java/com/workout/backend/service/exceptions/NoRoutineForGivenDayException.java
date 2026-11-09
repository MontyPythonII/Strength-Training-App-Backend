package com.workout.backend.service.exceptions;

public class NoRoutineForGivenDayException extends RuntimeException
{
    public NoRoutineForGivenDayException()
    {
        super("Your schedule has no routine for the given day");
    }
}

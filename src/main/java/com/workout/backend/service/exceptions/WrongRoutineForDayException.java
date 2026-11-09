package com.workout.backend.service.exceptions;

public class WrongRoutineForDayException extends RuntimeException
{
    public WrongRoutineForDayException()
    {
        super("This routine is not completable on this day");
    }
}

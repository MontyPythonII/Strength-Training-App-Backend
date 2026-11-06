package com.workout.backend.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Raised when an exercise id does not exist or does not belong to the requesting user. Both cases are reported identically.
 */
public class ExerciseNotFoundException extends RuntimeException
{
    public ExerciseNotFoundException(Long exerciseId)
    {
        super("Exercise " + exerciseId + " not found");
    }

    @ExceptionHandler(ExerciseNotFoundException.class)
    public ProblemDetail handleExerciseNotFound(ExerciseNotFoundException exception)
    {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

}


package com.workout.backend.service.exceptions;

public class ModelNotFoundException extends RuntimeException
{
    public ModelNotFoundException(Class<?> modelType, Long id)
    {
        super(modelType.getSimpleName() + " " + id + " not found");
    }
}

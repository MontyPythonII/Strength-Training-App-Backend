package com.workout.backend.controller.exceptions;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import tools.jackson.core.JacksonException.Reference;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.databind.exc.InvalidFormatException;
import com.workout.backend.service.exceptions.EmailAlreadyRegisteredException;
import com.workout.backend.service.exceptions.ModelNotFoundException;

/**
 * Translates exceptions escaping any controller into consistent ProblemDetail responses with the right HTTP status.
 * Extends ResponseEntityExceptionHandler so Spring Boot's own ProblemDetails advice backs off instead of shadowing these handlers.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler
{
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException exception,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    )
    {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation failed");
        problem.setProperty("errors", this.fieldErrors(exception));

        return ResponseEntity.badRequest().body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        HttpMessageNotReadableException exception,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    )
    {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, this.describeUnreadable(exception));

        return ResponseEntity.badRequest().body(problem);
    }

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

    @ExceptionHandler(ModelNotFoundException.class)
    public ProblemDetail handleNotFound(ModelNotFoundException exception)
    {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDenied(AccessDeniedException exception)
    {
        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Access denied");
    }

    // One field can fail several constraints, so the merge function keeps the first message rather than throwing on a duplicate key.
    private Map<String, String> fieldErrors(MethodArgumentNotValidException exception)
    {
        return exception.getBindingResult()
            .getFieldErrors()
            .stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                error -> Objects.requireNonNullElse(error.getDefaultMessage(), "invalid"),
                (first, second) -> first
            ));
    }

    // Jackson wraps every deserialization failure in the same exception, so the cause is what tells the client what went wrong.
    private String describeUnreadable(HttpMessageNotReadableException exception)
    {
        Throwable cause = exception.getCause();

        if (cause instanceof InvalidFormatException invalid)
        {
            String field = invalid.getPath()
                .stream()
                .map(Reference::getPropertyName)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("."));

            Class<?> target = invalid.getTargetType();

            if (target.isEnum())
            {
                return field + " must be one of " + Arrays.toString(target.getEnumConstants());
            }

            return field + " has the wrong type, expected " + target.getSimpleName();
        }

        if (cause instanceof StreamReadException)
        {
            return "Request body is not valid JSON";
        }

        return "Request body is missing or unreadable";
    }
}

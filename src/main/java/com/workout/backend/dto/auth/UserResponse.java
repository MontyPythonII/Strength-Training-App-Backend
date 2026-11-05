package com.workout.backend.dto.auth;

import com.workout.backend.model.users.User;
import com.workout.backend.model.users.UserType;

public record UserResponse
(
    Long id,
    String email,
    String displayName,
    UserType userType
)
{
    public static UserResponse from(User user)
    {
        return new UserResponse(user.getId(), user.getEmail(), user.getDisplayName(), user.getUserType());
    }
}

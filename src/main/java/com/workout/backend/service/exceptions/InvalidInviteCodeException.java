package com.workout.backend.service.exceptions;

/**
 * Raised when an invite code matches no group. Unknown and regenerated codes are reported identically so a client can't probe which codes once existed.
 */
public class InvalidInviteCodeException extends RuntimeException
{
    public InvalidInviteCodeException()
    {
        super("Invite code is invalid or has expired");
    }
}


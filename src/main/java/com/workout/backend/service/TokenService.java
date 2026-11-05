package com.workout.backend.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.workout.backend.config.JwtProperties;
import com.workout.backend.model.users.User;

/**
 * Issues signed access tokens. Subject is the user id so controllers can resolve the owner without a DB lookup.
 */
@Service
public class TokenService
{
    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    public TokenService(JwtEncoder jwtEncoder, JwtProperties jwtProperties)
    {
        this.jwtEncoder = jwtEncoder;
        this.jwtProperties = jwtProperties;
    }

    public String issueToken(User user)
    {
        Instant now = Instant.now();
        
        JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("workout-backend")
            .issuedAt(now)
            .expiresAt(now.plus(this.jwtProperties.expiryMinutes(), ChronoUnit.MINUTES))
            .subject(String.valueOf(user.getId()))
            .claim("roles", List.of(user.getUserType().name()))
            .build();
        
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}

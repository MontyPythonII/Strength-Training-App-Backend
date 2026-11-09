package com.workout.backend.controller.coach;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.workout.backend.security.CoachOnly;

@RestController
@RequestMapping("/api/coach/athletes")
@CoachOnly
public class CoachAthleteController
{
    
}

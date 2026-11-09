package com.workout.backend.controller.coach;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.workout.backend.dto.athleteGroup.AthleteGroupRequest;
import com.workout.backend.dto.athleteGroup.AthleteGroupResponse;
import com.workout.backend.security.CoachOnly;
import com.workout.backend.security.CurrentUserId;
import com.workout.backend.service.AthleteGroupService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/coach/athlete-groups")
@CoachOnly
public class CoachAthleteGroupController
{
    private final AthleteGroupService athleteGroupService;

    public CoachAthleteGroupController(
        AthleteGroupService athleteGroupService
    )
    {
        this.athleteGroupService = athleteGroupService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AthleteGroupResponse createGroup(
        @RequestBody @Valid AthleteGroupRequest request,
        @CurrentUserId Long userId
    )
    {
        return this.athleteGroupService.create(userId, request);
    }

    @GetMapping("{id}")
    public AthleteGroupResponse getGroup(
        @PathVariable Long id,
        @CurrentUserId Long userId
    )
    {
        return this.athleteGroupService.get(id, userId);
    }

    @GetMapping
    public List<AthleteGroupResponse> listGroups(
        @CurrentUserId Long userId
    )
    {
        return this.athleteGroupService.get(userId);
    }
}

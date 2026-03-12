package com.savaleOx.hockeyteam.controller;

import com.savaleOx.hockeyteam.dto.TeamRequestDto;
import com.savaleOx.hockeyteam.dto.TeamResponseDto;
import com.savaleOx.hockeyteam.service.TeamService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public List<TeamResponseDto> getAll() {
        return teamService.getAll();
    }

    @GetMapping("/{id}")
    public TeamResponseDto getById(@PathVariable Long id) {
        return teamService.getById(id);
    }

    @PostMapping
    public TeamResponseDto create(@RequestBody TeamRequestDto dto) {
        return teamService.create(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        teamService.delete(id);
    }
}
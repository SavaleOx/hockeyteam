package com.savaleox.hockeyteam.controller;

import com.savaleox.hockeyteam.dto.TeamRequestDto;
import com.savaleox.hockeyteam.dto.TeamResponseDto;
import com.savaleox.hockeyteam.service.TeamService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PutMapping("/{id}")
    public TeamResponseDto update(@PathVariable Long id, @RequestBody TeamRequestDto dto) {
        return teamService.update(id, dto);
    }

    @PatchMapping("/{id}")
    public TeamResponseDto patch(@PathVariable Long id, @RequestBody TeamRequestDto dto) {
        return teamService.patch(id, dto);
    }
}
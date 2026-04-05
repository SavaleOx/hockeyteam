package com.savaleox.hockeyteam.controller;

import com.savaleox.hockeyteam.dto.PlayerRequestDto;
import com.savaleox.hockeyteam.dto.PlayerResponseDto;
import com.savaleox.hockeyteam.service.PlayerService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/{id}")
    public PlayerResponseDto getById(@PathVariable Long id) {
        return playerService.getById(id);
    }

    @GetMapping
    public List<PlayerResponseDto> getPlayers(
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) String position,
            @RequestParam(required = false, name = "min-goals") Integer minGoals) {

        if (teamId != null && position != null) {
            return playerService.getByTeamAndPosition(teamId, position);
        }
        if (teamId != null) {
            return playerService.getByTeam(teamId);
        }
        if (position != null) {
            return playerService.getByPosition(position);
        }
        if (minGoals != null) {
            return playerService.getByMinGoals(minGoals);
        }
        return playerService.getAll();
    }

    @PostMapping
    public PlayerResponseDto create(@RequestBody PlayerRequestDto dto) {
        return playerService.create(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        playerService.delete(id);
    }

    @PutMapping("/{id}")
    public PlayerResponseDto update(@PathVariable Long id, @RequestBody PlayerRequestDto dto) {
        return playerService.update(id, dto);
    }

    @PatchMapping("/{id}")
    public PlayerResponseDto patch(@PathVariable Long id, @RequestBody PlayerRequestDto dto) {
        return playerService.patch(id, dto);
    }
}
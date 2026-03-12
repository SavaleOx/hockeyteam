package com.savaleOx.hockeyteam.controller;

import com.savaleOx.hockeyteam.dto.PlayerRequestDto;
import com.savaleOx.hockeyteam.dto.PlayerResponseDto;
import com.savaleOx.hockeyteam.service.PlayerService;
import org.springframework.web.bind.annotation.*;
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
}
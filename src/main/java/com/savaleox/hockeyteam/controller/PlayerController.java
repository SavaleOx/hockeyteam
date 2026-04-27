package com.savaleox.hockeyteam.controller;

import com.savaleox.hockeyteam.dto.AchievementResponseDto;
import com.savaleox.hockeyteam.dto.PlayerRequestDto;
import com.savaleox.hockeyteam.dto.PlayerResponseDto;
import com.savaleox.hockeyteam.dto.PlayerSearchCriteria;
import com.savaleox.hockeyteam.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@Validated
@Tag(name = "Игроки", description = "эндпоинт для манипуляций над игроками")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение игроков по идентификатору")
    public PlayerResponseDto getById(@PathVariable Long id) {
        return playerService.getById(id);
    }

    @GetMapping
    @Operation(summary = "Получение игроков")
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
    @Operation(summary = "Создание игрока")
    public PlayerResponseDto create(@Valid @RequestBody PlayerRequestDto dto) {
        return playerService.create(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление игрока")
    public void delete(@PathVariable Long id) {
        playerService.delete(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Измение полей игрока")
    public PlayerResponseDto update(@PathVariable Long id, @Valid @RequestBody PlayerRequestDto dto) {
        return playerService.update(id, dto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Измение некоторых полей игрока")
    public PlayerResponseDto patch(@PathVariable Long id, @Valid @RequestBody PlayerRequestDto dto) {
        return playerService.patch(id, dto);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск игроков с фильтрацией через JPQL")
    public Page<PlayerResponseDto> searchPlayersJPQL(
            @ModelAttribute PlayerSearchCriteria criteria,
            Pageable pageable) {
        return playerService.searchPlayersJPQL(criteria, pageable);
    }

    @GetMapping("/search/native")
    @Operation(summary = "Поиск игроков с фильтрацией через native query")
    public Page<PlayerResponseDto> searchPlayersNative(
            @ModelAttribute PlayerSearchCriteria criteria,
            Pageable pageable) {
        return playerService.searchPlayersNative(criteria, pageable);
    }

    @PostMapping("/{playerId}/achievements/{achievementId}")
    @Operation(summary = "Добавить достижение игроку")
    public PlayerResponseDto addAchievement(
            @PathVariable Long playerId,
            @PathVariable Long achievementId) {
        return playerService.addAchievement(playerId, achievementId);
    }

    @DeleteMapping("/{playerId}/achievements/{achievementId}")
    @Operation(summary = "Удалить достижение игроку")
    public PlayerResponseDto removeAchievement(
            @PathVariable Long playerId,
            @PathVariable Long achievementId) {
        return playerService.removeAchievement(playerId, achievementId);
    }

    @GetMapping("/{playerId}/achievements")
    @Operation(summary = "Получить все достижения игрока")
    public List<AchievementResponseDto> getPlayerAchievements(
            @PathVariable Long playerId) {
        return playerService.getPlayerAchievements(playerId);
    }

    @PutMapping("/{playerId}/achievements")
    @Operation(summary = "Установить новые достижения игроку")
    public PlayerResponseDto setAchievements(
            @PathVariable Long playerId,
            @RequestBody List<Long> achievementIds) {
        return playerService.setAchievements(playerId, achievementIds);
    }
}
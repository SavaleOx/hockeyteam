package com.savaleox.hockeyteam.controller;

import com.savaleox.hockeyteam.dto.TeamRequestDto;
import com.savaleox.hockeyteam.dto.TeamResponseDto;
import com.savaleox.hockeyteam.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
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
@Validated
@Tag(name = "Команды", description = "эндпоинт для манипуляций командами")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    @Operation(summary = "Получение всех команд")
    public List<TeamResponseDto> getAll() {
        return teamService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение команды по идентификатору")
    public TeamResponseDto getById(@PathVariable Long id) {
        return teamService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Создание новой команды")
    public TeamResponseDto create(@Valid @RequestBody TeamRequestDto dto) {
        return teamService.create(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление команды")
    public void delete(@PathVariable Long id) {
        teamService.delete(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Полное обновление сведений о команде")
    public TeamResponseDto update(@PathVariable Long id, @Valid @RequestBody TeamRequestDto dto) {
        return teamService.update(id, dto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Частичное обновление сведений о команде")
    public TeamResponseDto patch(@PathVariable Long id, @RequestBody TeamRequestDto dto) {
        return teamService.patch(id, dto);
    }
}
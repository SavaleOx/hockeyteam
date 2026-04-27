package com.savaleox.hockeyteam.controller;

import com.savaleox.hockeyteam.dto.AchievementRequestDto;
import com.savaleox.hockeyteam.dto.AchievementResponseDto;
import com.savaleox.hockeyteam.service.AchievementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/achievements")
@Validated
@Tag(name = "Достижения", description = "эндпоинт для манипуляций над достижениями")
public class AchievementController {
    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping
    @Operation(summary = "Получить все существующие достижения")
    public List<AchievementResponseDto> getAll() {
        return achievementService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить достидение по идентификатору")
    public AchievementResponseDto getById(@PathVariable Long id) {
        return achievementService.getById(id);
    }

    @GetMapping("/by-name")
    @Operation(summary = "Получить достижение по наззванию")
    public AchievementResponseDto getByName(@RequestParam String name) {
        return achievementService.getByName(name);
    }

    @PostMapping
    @Operation(summary = "Создание достижения")
    public AchievementResponseDto create(@RequestBody AchievementRequestDto dto) {
        return achievementService.create(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление достижения")
    public void delete(@PathVariable Long id) {
        achievementService.delete(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Полное обновление достижения")
    public AchievementResponseDto update(@PathVariable Long id, @RequestBody AchievementRequestDto dto) {
        return achievementService.update(id, dto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Частичное обновление достижения")
    public AchievementResponseDto patch(@PathVariable Long id, @RequestBody AchievementRequestDto dto) {
        return achievementService.patch(id, dto);
    }
}
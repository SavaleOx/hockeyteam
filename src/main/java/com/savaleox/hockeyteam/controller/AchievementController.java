package com.savaleox.hockeyteam.controller;

import com.savaleox.hockeyteam.dto.AchievementRequestDto;
import com.savaleox.hockeyteam.dto.AchievementResponseDto;
import com.savaleox.hockeyteam.service.AchievementService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/achievements")
public class AchievementController {
    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping
    public List<AchievementResponseDto> getAll() {
        return achievementService.getAll();
    }

    @GetMapping("/{id}")
    public AchievementResponseDto getById(@PathVariable Long id) {
        return achievementService.getById(id);
    }

    @GetMapping("/by-name")
    public AchievementResponseDto getByName(@RequestParam String name) {
        return achievementService.getByName(name);
    }

    @PostMapping
    public AchievementResponseDto create(@RequestBody AchievementRequestDto dto) {
        return achievementService.create(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        achievementService.delete(id);
    }
}
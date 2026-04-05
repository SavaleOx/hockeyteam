package com.savaleox.hockeyteam.controller;

import com.savaleox.hockeyteam.dto.AchievementRequestDto;
import com.savaleox.hockeyteam.dto.AchievementResponseDto;
import com.savaleox.hockeyteam.service.AchievementService;
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

    @PutMapping("/{id}")
    public AchievementResponseDto update(@PathVariable Long id, @RequestBody AchievementRequestDto dto) {
        return achievementService.update(id, dto);
    }

    @PatchMapping("/{id}")
    public AchievementResponseDto patch(@PathVariable Long id, @RequestBody AchievementRequestDto dto) {
        return achievementService.patch(id, dto);
    }
}
package com.savaleox.hockeyteam.controller;

import com.savaleox.hockeyteam.dto.CoachRequestDto;
import com.savaleox.hockeyteam.dto.CoachResponseDto;
import com.savaleox.hockeyteam.service.CoachService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/coaches")
@Tag(name = "Тренера", description = "эндпоинт для манипуляций над тренерами")
public class CoachController {

    private final CoachService coachService;

    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping
    public List<CoachResponseDto> getAll() {
        return coachService.getAll();
    }

    @GetMapping("/{id}")
    public CoachResponseDto getById(@PathVariable Long id) {
        return coachService.getById(id);
    }

    @GetMapping("/by-team")
    public List<CoachResponseDto> getByTeam(@RequestParam Long teamId) {
        return coachService.getByTeam(teamId);
    }

    @PostMapping
    public CoachResponseDto create(@Valid @RequestBody CoachRequestDto dto) {
        return coachService.create(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        coachService.delete(id);
    }

    @PutMapping("/{id}")
    public CoachResponseDto update(@PathVariable Long id, @Valid @RequestBody CoachRequestDto dto) {
        return coachService.update(id, dto);
    }

    @PatchMapping("/{id}")
    public CoachResponseDto patch(@PathVariable Long id, @RequestBody CoachRequestDto dto) {
        return coachService.patch(id, dto);
    }
}

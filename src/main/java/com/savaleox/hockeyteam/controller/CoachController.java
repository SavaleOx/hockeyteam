package com.savaleox.hockeyteam.controller;

import com.savaleox.hockeyteam.dto.CoachRequestDto;
import com.savaleox.hockeyteam.dto.CoachResponseDto;
import com.savaleox.hockeyteam.service.CoachService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/coaches")
@Validated
@Tag(name = "Тренера", description = "эндпоинт для манипуляций над тренерами")
public class CoachController {

    private final CoachService coachService;

    public CoachController(CoachService coachService) {
        this.coachService = coachService;
    }

    @GetMapping
    @Operation(summary = "Получение всех тренеров")
    public List<CoachResponseDto> getAll() {
        return coachService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение тренера по идентификатору")
    public CoachResponseDto getById(@PathVariable Long id) {
        return coachService.getById(id);
    }

    @GetMapping("/by-team")
    @Operation(summary = "Получение тренера определённой команды")
    public List<CoachResponseDto> getByTeam(@RequestParam Long teamId) {
        return coachService.getByTeam(teamId);
    }

    @PostMapping
    @Operation(summary = "Создание тренера")
    public CoachResponseDto create(@Valid @RequestBody CoachRequestDto dto) {
        return coachService.create(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление тренера")
    public void delete(@PathVariable Long id) {
        coachService.delete(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Полное обновление сведений о тренере")
    public CoachResponseDto update(@PathVariable Long id, @Valid @RequestBody CoachRequestDto dto) {
        return coachService.update(id, dto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Частисчное обновление сведений о тренере")
    public CoachResponseDto patch(@PathVariable Long id, @RequestBody CoachRequestDto dto) {
        return coachService.patch(id, dto);
    }
}

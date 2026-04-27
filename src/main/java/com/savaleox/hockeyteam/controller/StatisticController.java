package com.savaleox.hockeyteam.controller;

import com.savaleox.hockeyteam.dto.StatisticRequestDto;
import com.savaleox.hockeyteam.dto.StatisticResponseDto;
import com.savaleox.hockeyteam.service.StatisticService;
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
@RequestMapping("/statistics")
@Validated
@Tag(name = "Статистика", description = "эндпоинт для манипуляций статистикой")
public class StatisticController {
    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping
    @Operation(summary = "Получение статистики определённого игрока за определённый сезон")
    public List<StatisticResponseDto> getStatistics(
            @RequestParam Long playerId,
            @RequestParam(required = false) Integer season) {

        if (season != null) {
            return statisticService.getByPlayerAndSeason(playerId, season);
        }
        return statisticService.getByPlayer(playerId);
    }

    @PostMapping
    @Operation(summary = "Создание статистики")
    public StatisticResponseDto create(@RequestBody StatisticRequestDto dto) {
        return statisticService.create(dto);
    }

    @PostMapping("/no-transactional")
    @Operation(summary = "Демонстрационное создание статистики без транзакционности")
    public StatisticResponseDto createWithoutTransactional(@RequestBody StatisticRequestDto dto) {
        return statisticService.createWithoutTransactional(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление одной статистической записи")
    public void delete(@PathVariable Long id) {
        statisticService.delete(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Полное обновление статистических вседений")
    public StatisticResponseDto update(@PathVariable Long id, @RequestBody StatisticRequestDto dto) {
        return statisticService.update(id, dto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Частичное обновление статистической записи")
    public StatisticResponseDto patch(@PathVariable Long id, @RequestBody StatisticRequestDto dto) {
        return statisticService.patch(id, dto);
    }
}
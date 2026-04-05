package com.savaleox.hockeyteam.controller;

import com.savaleox.hockeyteam.dto.StatisticRequestDto;
import com.savaleox.hockeyteam.dto.StatisticResponseDto;
import com.savaleox.hockeyteam.service.StatisticService;
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
public class StatisticController {
    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping
    public List<StatisticResponseDto> getStatistics(
            @RequestParam Long playerId,
            @RequestParam(required = false) Integer season) {

        if (season != null) {
            return statisticService.getByPlayerAndSeason(playerId, season);
        }
        return statisticService.getByPlayer(playerId);
    }

    @PostMapping
    public StatisticResponseDto create(@RequestBody StatisticRequestDto dto) {
        return statisticService.create(dto);
    }

    @PostMapping("/no-transactional")
    public StatisticResponseDto createWithoutTransactional(@RequestBody StatisticRequestDto dto) {
        return statisticService.createWithoutTransactional(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        statisticService.delete(id);
    }

    @PutMapping("/{id}")
    public StatisticResponseDto update(@PathVariable Long id, @RequestBody StatisticRequestDto dto) {
        return statisticService.update(id, dto);
    }

    @PatchMapping("/{id}")
    public StatisticResponseDto patch(@PathVariable Long id, @RequestBody StatisticRequestDto dto) {
        return statisticService.patch(id, dto);
    }
}
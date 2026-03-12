package com.savaleOx.hockeyteam.controller;

import com.savaleOx.hockeyteam.dto.StatisticRequestDto;
import com.savaleOx.hockeyteam.dto.StatisticResponseDto;
import com.savaleOx.hockeyteam.service.StatisticService;
import org.springframework.web.bind.annotation.*;
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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        statisticService.delete(id);
    }
}
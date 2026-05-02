package com.savaleox.hockeyteam.controller;

import com.savaleox.hockeyteam.dto.AsyncTaskMetricsDto;
import com.savaleox.hockeyteam.dto.AsyncTaskStatusDto;
import com.savaleox.hockeyteam.dto.AsyncTaskSubmissionDto;
import com.savaleox.hockeyteam.service.AsyncStatisticService;
import com.savaleox.hockeyteam.service.AsyncTaskCounterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/async/statistics")
@Tag(name = "Асинхронная статистика", description = "Асинхронный расчёт статистики игрока")
public class AsyncStatisticController {

    private final AsyncStatisticService asyncStatisticService;
    private final AsyncTaskCounterService asyncTaskCounterService;

    public AsyncStatisticController(AsyncStatisticService asyncStatisticService,
                                    AsyncTaskCounterService asyncTaskCounterService) {
        this.asyncStatisticService = asyncStatisticService;
        this.asyncTaskCounterService = asyncTaskCounterService;
    }

    @PostMapping
    @Operation(summary = "Запустить асинхронный расчёт статистики")
    public ResponseEntity<AsyncTaskSubmissionDto> start(
            @Parameter(description = "ID игрока", example = "1") @RequestParam Long playerId,
            @Parameter(description = "Сезон", example = "2026") @RequestParam Integer season,
            @Parameter(description = "Голы", example = "15") @RequestParam Integer goals,
            @Parameter(description = "Ассисты", example = "25") @RequestParam Integer assists,
            @Parameter(description = "Игры", example = "70") @RequestParam Integer games
    ) {
        String taskId = asyncStatisticService.startStatisticCalculation(playerId, season, goals, assists, games);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new AsyncTaskSubmissionDto(taskId));
    }

    @GetMapping("/{taskId}")
    @Operation(summary = "Получить статус асинхронной задачи")
    public ResponseEntity<AsyncTaskStatusDto> status(@PathVariable String taskId) {
        return ResponseEntity.ok(asyncStatisticService.getStatus(taskId).orElseThrow());
    }

    @GetMapping("/metrics")
    @Operation(summary = "Получить метрики асинхронных задач")
    public ResponseEntity<AsyncTaskMetricsDto> metrics() {
        return ResponseEntity.ok(asyncTaskCounterService.getMetrics());
    }
}
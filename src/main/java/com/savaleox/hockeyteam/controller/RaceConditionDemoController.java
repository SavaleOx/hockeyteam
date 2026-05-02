package com.savaleox.hockeyteam.controller;

import com.savaleox.hockeyteam.dto.RaceConditionDemoDto;
import com.savaleox.hockeyteam.service.RaceConditionDemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concurrency")
@Tag(name = "Concurrency", description = "Демонстрация race condition и синхронизации")
public class RaceConditionDemoController {

    private final RaceConditionDemoService raceConditionDemoService;

    public RaceConditionDemoController(RaceConditionDemoService raceConditionDemoService) {
        this.raceConditionDemoService = raceConditionDemoService;
    }

    @GetMapping("/race-demo")
    @Operation(summary = "Запустить демонстрацию race condition с 50+ потоками")
    public ResponseEntity<RaceConditionDemoDto> runRaceDemo(
            @Parameter(description = "Количество потоков (минимум 50)", example = "64")
            @RequestParam(defaultValue = "64") int threads,
            @Parameter(description = "Количество инкрементов на поток", example = "10000")
            @RequestParam(defaultValue = "10000") int incrementsPerThread
    ) {
        return ResponseEntity.ok(raceConditionDemoService.runDemo(threads, incrementsPerThread));
    }
}
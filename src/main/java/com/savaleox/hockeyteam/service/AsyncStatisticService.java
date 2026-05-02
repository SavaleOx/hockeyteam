package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.AsyncTaskStatusDto;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AsyncStatisticService {

    private final AsyncTaskRegistryService asyncTaskRegistryService;
    private final AsyncStatisticWorkerService asyncStatisticWorkerService;
    private final AsyncTaskCounterService asyncTaskCounterService;

    public AsyncStatisticService(AsyncTaskRegistryService asyncTaskRegistryService,
                                 AsyncStatisticWorkerService asyncStatisticWorkerService,
                                 AsyncTaskCounterService asyncTaskCounterService) {
        this.asyncTaskRegistryService = asyncTaskRegistryService;
        this.asyncStatisticWorkerService = asyncStatisticWorkerService;
        this.asyncTaskCounterService = asyncTaskCounterService;
    }

    public String startStatisticCalculation(Long playerId, Integer season,
                                            Integer goals, Integer assists, Integer games) {
        String taskId = UUID.randomUUID().toString();
        asyncTaskRegistryService.putPending(taskId);
        asyncTaskCounterService.incrementSubmitted();
        asyncStatisticWorkerService.runTask(taskId, playerId, season, goals, assists, games);
        return taskId;
    }

    public Optional<AsyncTaskStatusDto> getStatus(String taskId) {
        return asyncTaskRegistryService.getStatus(taskId);
    }
}
package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.config.AsyncProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AsyncStatisticWorkerService {

    private final StatisticService statisticService;
    private final AsyncTaskRegistryService asyncTaskRegistryService;
    private final AsyncTaskCounterService asyncTaskCounterService;
    private final long statisticDelayMs;

    public AsyncStatisticWorkerService(StatisticService statisticService,
                                       AsyncTaskRegistryService asyncTaskRegistryService,
                                       AsyncTaskCounterService asyncTaskCounterService,
                                       AsyncProperties asyncProperties) {
        this.statisticService = statisticService;
        this.asyncTaskRegistryService = asyncTaskRegistryService;
        this.asyncTaskCounterService = asyncTaskCounterService;
        this.statisticDelayMs = asyncProperties.getStatisticDelayMs();
    }

    @Async
    public CompletableFuture<Void> runTask(String taskId, Long playerId, Integer season,
                                           Integer goals, Integer assists, Integer games) {
        asyncTaskRegistryService.markRunning(taskId, "Расчёт статистики выполняется");
        asyncTaskCounterService.incrementRunning();
        try {
            doSleep(statisticDelayMs);
            asyncTaskRegistryService.markSuccess(taskId,
                    String.format("Статистика для игрока %d за сезон %d успешно рассчитана", playerId, season));
            asyncTaskCounterService.incrementSucceeded();
            return CompletableFuture.completedFuture(null);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            log.error("Async statistic calculation interrupted for taskId={} playerId={}", taskId, playerId, ex);
            asyncTaskRegistryService.markFailed(taskId, "Task interrupted");
            asyncTaskCounterService.incrementFailed();
            return CompletableFuture.failedFuture(ex);
        } catch (Exception ex) {
            log.error("Async statistic calculation failed for taskId={} playerId={}", taskId, playerId, ex);
            asyncTaskRegistryService.markFailed(taskId, ex.getMessage());
            asyncTaskCounterService.incrementFailed();
            return CompletableFuture.failedFuture(ex);
        } finally {
            asyncTaskCounterService.decrementRunning();
        }
    }

    protected void doSleep(long millis) throws InterruptedException {
        Thread.sleep(millis);
    }
}
package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.config.AsyncProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsyncStatisticWorkerServiceTest {

    @Mock
    private StatisticService statisticService;

    @Mock
    private AsyncTaskRegistryService asyncTaskRegistryService;

    @Mock
    private AsyncTaskCounterService asyncTaskCounterService;

    @Mock
    private AsyncProperties asyncProperties;

    private AsyncStatisticWorkerService service;

    @BeforeEach
    void setUp() {
        when(asyncProperties.getStatisticDelayMs()).thenReturn(100L);
        service = spy(new AsyncStatisticWorkerService(
                statisticService,
                asyncTaskRegistryService,
                asyncTaskCounterService,
                asyncProperties
        ));
    }

    @Test
    void runTask_shouldCompleteSuccessfully() throws Exception {
        service.runTask("task-1", 1L, 2026, 15, 25, 70).get();

        verify(asyncTaskRegistryService).markRunning("task-1", "Расчёт статистики выполняется");
        verify(asyncTaskCounterService).incrementRunning();
        verify(asyncTaskRegistryService).markSuccess(eq("task-1"), anyString());
        verify(asyncTaskCounterService).incrementSucceeded();
        verify(asyncTaskCounterService).decrementRunning();
    }

    @Test
    void runTask_shouldCallDecrementRunningOnSuccess() throws Exception {
        service.runTask("task-success", 2L, 2025, 10, 20, 60).get();

        verify(asyncTaskCounterService).decrementRunning();
    }

    @Test
    void runTask_shouldFailWhenMarkRunningThrowsException() {
        doThrow(new RuntimeException("Test exception"))
                .when(asyncTaskRegistryService)
                .markRunning(anyString(), anyString());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.runTask("task-fail", 3L, 2024, 5, 10, 50).get();
        });

        assertEquals("Test exception", exception.getMessage());

        verify(asyncTaskRegistryService).markRunning(eq("task-fail"), eq("Расчёт статистики выполняется"));
        verify(asyncTaskRegistryService, never()).markFailed(anyString(), anyString());
        verify(asyncTaskCounterService, never()).incrementFailed();
        verify(asyncTaskCounterService, never()).decrementRunning();
    }

    @Test
    void runTask_shouldStoreSuccessMessage() throws Exception {
        service.runTask("task-msg", 1L, 2026, 15, 25, 70).get();

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(asyncTaskRegistryService).markSuccess(eq("task-msg"), messageCaptor.capture());

        String message = messageCaptor.getValue();
        assertTrue(message.contains("1"));
        assertTrue(message.contains("2026"));
        assertTrue(message.contains("успешно рассчитана"));
    }

    @Test
    void runTask_shouldHandleMultipleTasks() throws Exception {
        service.runTask("task-a", 1L, 2026, 10, 10, 50).get();
        service.runTask("task-b", 2L, 2025, 20, 20, 60).get();

        verify(asyncTaskRegistryService).markSuccess(eq("task-a"), anyString());
        verify(asyncTaskRegistryService).markSuccess(eq("task-b"), anyString());
        verify(asyncTaskCounterService, org.mockito.Mockito.times(2)).incrementSucceeded();
        verify(asyncTaskCounterService, org.mockito.Mockito.times(2)).decrementRunning();
    }

    @Test
    void runTask_shouldHandleInterruptedException() throws Exception {
        InterruptedException interruptedException = new InterruptedException("sleep interrupted");
        doThrow(interruptedException).when(service).doSleep(anyLong());

        CompletableFuture<Void> future = service.runTask("task-interrupt", 1L, 2026, 15, 25, 70);

        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertTrue(exception.getCause() instanceof InterruptedException);

        verify(asyncTaskRegistryService).markRunning("task-interrupt", "Расчёт статистики выполняется");
        verify(asyncTaskCounterService).incrementRunning();
        verify(asyncTaskRegistryService).markFailed(eq("task-interrupt"), eq("Task interrupted"));
        verify(asyncTaskCounterService).incrementFailed();
        verify(asyncTaskCounterService).decrementRunning();
    }

    @Test
    void runTask_shouldHandleGeneralException() throws Exception {
        RuntimeException testException = new RuntimeException("Database connection failed");
        doThrow(testException).when(service).doSleep(anyLong());

        CompletableFuture<Void> future = service.runTask("task-exception", 5L, 2025, 10, 20, 60);

        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertEquals(testException, exception.getCause());

        verify(asyncTaskRegistryService).markRunning("task-exception", "Расчёт статистики выполняется");
        verify(asyncTaskCounterService).incrementRunning();
        verify(asyncTaskRegistryService).markFailed(eq("task-exception"), eq("Database connection failed"));
        verify(asyncTaskCounterService).incrementFailed();
        verify(asyncTaskCounterService).decrementRunning();
    }

    @Test
    void runTask_shouldReturnFailedFutureOnException() throws Exception {
        RuntimeException testException = new RuntimeException("Test error");
        doThrow(testException).when(service).doSleep(anyLong());

        CompletableFuture<Void> future = service.runTask("task-future", 1L, 2026, 10, 10, 50);

        assertTrue(future.isCompletedExceptionally());
    }

    @Test
    void runTask_shouldSetInterruptFlagOnInterruptedException() throws Exception {
        InterruptedException interruptedException = new InterruptedException("sleep interrupted");
        doThrow(interruptedException).when(service).doSleep(anyLong());

        service.runTask("task-flag", 1L, 2026, 15, 25, 70);

        assertTrue(Thread.interrupted());
    }
}
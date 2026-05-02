package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.AsyncTaskStatusDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsyncStatisticServiceTest {

    @Mock
    private AsyncTaskRegistryService asyncTaskRegistryService;

    @Mock
    private AsyncStatisticWorkerService asyncStatisticWorkerService;

    @Mock
    private AsyncTaskCounterService asyncTaskCounterService;

    @InjectMocks
    private AsyncStatisticService service;

    @Test
    void startStatisticCalculation_shouldReturnTaskId() {
        String taskId = service.startStatisticCalculation(1L, 2026, 15, 25, 70);

        assertNotNull(taskId);
        UUID.fromString(taskId);
    }

    @Test
    void startStatisticCalculation_shouldPutPendingTask() {
        String taskId = service.startStatisticCalculation(1L, 2026, 15, 25, 70);

        verify(asyncTaskRegistryService).putPending(taskId);
    }

    @Test
    void startStatisticCalculation_shouldIncrementSubmitted() {
        service.startStatisticCalculation(1L, 2026, 15, 25, 70);

        verify(asyncTaskCounterService).incrementSubmitted();
    }

    @Test
    void startStatisticCalculation_shouldRunWorkerTask() {
        String taskId = service.startStatisticCalculation(1L, 2026, 15, 25, 70);

        verify(asyncStatisticWorkerService).runTask(taskId, 1L, 2026, 15, 25, 70);
    }

    @Test
    void startStatisticCalculation_shouldGenerateUniqueTaskIds() {
        String taskId1 = service.startStatisticCalculation(1L, 2026, 15, 25, 70);
        String taskId2 = service.startStatisticCalculation(2L, 2025, 20, 30, 60);

        assertNotNull(taskId1);
        assertNotNull(taskId2);
        assertNotEquals(taskId1, taskId2);
    }

    @Test
    void getStatus_shouldReturnStatusFromRegistry() {
        AsyncTaskStatusDto expectedStatus = new AsyncTaskStatusDto("task-1", "SUCCESS", "Done");
        when(asyncTaskRegistryService.getStatus("task-1")).thenReturn(Optional.of(expectedStatus));

        Optional<AsyncTaskStatusDto> result = service.getStatus("task-1");

        assertTrue(result.isPresent());
        assertEquals(expectedStatus, result.get());
    }

    @Test
    void getStatus_shouldReturnEmptyWhenNotFound() {
        when(asyncTaskRegistryService.getStatus("unknown")).thenReturn(Optional.empty());

        Optional<AsyncTaskStatusDto> result = service.getStatus("unknown");

        assertTrue(result.isEmpty());
    }

    @Test
    void fullFlow_shouldCreateAndTrackTask() {
        AsyncTaskStatusDto runningStatus = new AsyncTaskStatusDto("tracked-task", "RUNNING", "Processing");

        String taskId = service.startStatisticCalculation(5L, 2024, 10, 15, 50);
        when(asyncTaskRegistryService.getStatus(taskId)).thenReturn(Optional.of(runningStatus));

        Optional<AsyncTaskStatusDto> status = service.getStatus(taskId);

        assertTrue(status.isPresent());
        assertEquals("RUNNING", status.get().getStatus());
        verify(asyncTaskRegistryService).putPending(taskId);
    }
}
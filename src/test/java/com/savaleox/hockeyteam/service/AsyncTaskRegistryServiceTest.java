package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.AsyncTaskStatusDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AsyncTaskRegistryServiceTest {

    private AsyncTaskRegistryService service;

    @BeforeEach
    void setUp() {
        service = new AsyncTaskRegistryService();
    }

    @Test
    void putPending_shouldStoreTaskWithPendingStatus() {
        service.putPending("task-1");

        Optional<AsyncTaskStatusDto> status = service.getStatus("task-1");

        assertTrue(status.isPresent());
        assertEquals("task-1", status.get().getTaskId());
        assertEquals("PENDING", status.get().getStatus());
        assertEquals("Task accepted", status.get().getMessage());
    }

    @Test
    void getStatus_shouldReturnEmptyForUnknownTask() {
        Optional<AsyncTaskStatusDto> status = service.getStatus("unknown-task");

        assertFalse(status.isPresent());
    }

    @Test
    void markRunning_shouldUpdateStatusToRunning() {
        service.putPending("task-1");
        service.markRunning("task-1", "Task is running");

        Optional<AsyncTaskStatusDto> status = service.getStatus("task-1");

        assertTrue(status.isPresent());
        assertEquals("task-1", status.get().getTaskId());
        assertEquals("RUNNING", status.get().getStatus());
        assertEquals("Task is running", status.get().getMessage());
    }

    @Test
    void markRunning_shouldNotCreateNewTaskIfNotExists() {
        service.markRunning("nonexistent", "Running");

        Optional<AsyncTaskStatusDto> status = service.getStatus("nonexistent");

        assertFalse(status.isPresent());
    }

    @Test
    void markSuccess_shouldUpdateStatusToSuccess() {
        service.putPending("task-1");
        service.markSuccess("task-1", "Completed successfully");

        Optional<AsyncTaskStatusDto> status = service.getStatus("task-1");

        assertTrue(status.isPresent());
        assertEquals("task-1", status.get().getTaskId());
        assertEquals("SUCCESS", status.get().getStatus());
        assertEquals("Completed successfully", status.get().getMessage());
    }

    @Test
    void markSuccess_shouldNotCreateNewTaskIfNotExists() {
        service.markSuccess("nonexistent", "Success");

        Optional<AsyncTaskStatusDto> status = service.getStatus("nonexistent");

        assertFalse(status.isPresent());
    }

    @Test
    void markFailed_shouldUpdateStatusToFailed() {
        service.putPending("task-1");
        service.markFailed("task-1", "Something went wrong");

        Optional<AsyncTaskStatusDto> status = service.getStatus("task-1");

        assertTrue(status.isPresent());
        assertEquals("task-1", status.get().getTaskId());
        assertEquals("FAILED", status.get().getStatus());
        assertEquals("Something went wrong", status.get().getMessage());
    }

    @Test
    void markFailed_shouldNotCreateNewTaskIfNotExists() {
        service.markFailed("nonexistent", "Failed");

        Optional<AsyncTaskStatusDto> status = service.getStatus("nonexistent");

        assertFalse(status.isPresent());
    }

    @Test
    void fullLifecycle_shouldTransitionThroughAllStatuses() {
        service.putPending("lifecycle-task");

        assertEquals("PENDING", service.getStatus("lifecycle-task").get().getStatus());

        service.markRunning("lifecycle-task", "Processing");
        assertEquals("RUNNING", service.getStatus("lifecycle-task").get().getStatus());

        service.markSuccess("lifecycle-task", "Done");
        assertEquals("SUCCESS", service.getStatus("lifecycle-task").get().getStatus());
    }

    @Test
    void fullLifecycleWithFailure_shouldTransitionToFailed() {
        service.putPending("fail-task");

        assertEquals("PENDING", service.getStatus("fail-task").get().getStatus());

        service.markRunning("fail-task", "Processing");
        assertEquals("RUNNING", service.getStatus("fail-task").get().getStatus());

        service.markFailed("fail-task", "Error occurred");
        assertEquals("FAILED", service.getStatus("fail-task").get().getStatus());
    }

    @Test
    void multipleTasks_shouldBeIndependent() {
        service.putPending("task-a");
        service.putPending("task-b");

        service.markRunning("task-a", "A running");
        service.markSuccess("task-b", "B done");

        assertEquals("RUNNING", service.getStatus("task-a").get().getStatus());
        assertEquals("SUCCESS", service.getStatus("task-b").get().getStatus());
    }
}
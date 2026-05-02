package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.AsyncTaskMetricsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AsyncTaskCounterServiceTest {

    private AsyncTaskCounterService service;

    @BeforeEach
    void setUp() {
        service = new AsyncTaskCounterService();
    }

    @Test
    void incrementSubmitted_shouldIncrementCounter() {
        service.incrementSubmitted();
        service.incrementSubmitted();

        AsyncTaskMetricsDto metrics = service.getMetrics();
        assertEquals(2, metrics.getSubmitted());
    }

    @Test
    void incrementRunning_shouldIncrementCounter() {
        service.incrementRunning();
        service.incrementRunning();
        service.incrementRunning();

        AsyncTaskMetricsDto metrics = service.getMetrics();
        assertEquals(3, metrics.getRunning());
    }

    @Test
    void decrementRunning_shouldDecrementCounter() {
        service.incrementRunning();
        service.incrementRunning();
        service.decrementRunning();

        AsyncTaskMetricsDto metrics = service.getMetrics();
        assertEquals(1, metrics.getRunning());
    }

    @Test
    void decrementRunning_shouldNotGoBelowZero() {
        service.decrementRunning();
        service.decrementRunning();

        AsyncTaskMetricsDto metrics = service.getMetrics();
        assertEquals(0, metrics.getRunning());
    }

    @Test
    void decrementRunning_shouldReturnImmediatelyWhenAlreadyZero() {
        service.decrementRunning();

        AsyncTaskMetricsDto metrics = service.getMetrics();
        assertEquals(0, metrics.getRunning());
    }

    @Test
    void decrementRunning_shouldRetryWhenCompareAndSetFails() throws Exception {
        Field runningField = AsyncTaskCounterService.class.getDeclaredField("running");
        runningField.setAccessible(true);

        AtomicLong testRunning = new AtomicLong(2);
        runningField.set(service, testRunning);

        service.decrementRunning();

        assertEquals(1, testRunning.get());
    }

    @Test
    void decrementRunning_shouldDecrementFromOneToZero() {
        service.incrementRunning();
        service.decrementRunning();

        AsyncTaskMetricsDto metrics = service.getMetrics();
        assertEquals(0, metrics.getRunning());
    }

    @Test
    void decrementRunning_shouldHandleMultipleConcurrentDecrements() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            service.incrementRunning();
        }

        Thread[] threads = new Thread[10];
        for (int i = 0; i < 10; i++) {
            threads[i] = new Thread(() -> service.decrementRunning());
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        AsyncTaskMetricsDto metrics = service.getMetrics();
        assertEquals(0, metrics.getRunning());
    }

    @Test
    void incrementSucceeded_shouldIncrementCounter() {
        service.incrementSucceeded();
        service.incrementSucceeded();
        service.incrementSucceeded();

        AsyncTaskMetricsDto metrics = service.getMetrics();
        assertEquals(3, metrics.getSucceeded());
    }

    @Test
    void incrementFailed_shouldIncrementCounter() {
        service.incrementFailed();

        AsyncTaskMetricsDto metrics = service.getMetrics();
        assertEquals(1, metrics.getFailed());
    }

    @Test
    void getMetrics_shouldReturnAllCounters() {
        service.incrementSubmitted();
        service.incrementSubmitted();
        service.incrementRunning();
        service.decrementRunning();
        service.incrementSucceeded();
        service.incrementSucceeded();
        service.incrementFailed();

        AsyncTaskMetricsDto metrics = service.getMetrics();

        assertEquals(2, metrics.getSubmitted());
        assertEquals(0, metrics.getRunning());
        assertEquals(2, metrics.getSucceeded());
        assertEquals(1, metrics.getFailed());
    }

    @Test
    void getMetrics_shouldReturnZerosForNewService() {
        AsyncTaskMetricsDto metrics = service.getMetrics();

        assertEquals(0, metrics.getSubmitted());
        assertEquals(0, metrics.getRunning());
        assertEquals(0, metrics.getSucceeded());
        assertEquals(0, metrics.getFailed());
    }

    @Test
    void concurrentIncrements_shouldMaintainCorrectCounts() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                service.incrementSubmitted();
                service.incrementSucceeded();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                service.incrementSubmitted();
                service.incrementFailed();
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        AsyncTaskMetricsDto metrics = service.getMetrics();
        assertEquals(10000, metrics.getSubmitted());
        assertEquals(5000, metrics.getSucceeded());
        assertEquals(5000, metrics.getFailed());
    }
}
package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.RaceConditionDemoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

class RaceConditionDemoServiceTest {

    private RaceConditionDemoService service;

    @BeforeEach
    void setUp() {
        service = new RaceConditionDemoService();
    }

    @Test
    void runDemo_shouldDetectRaceCondition() {
        RaceConditionDemoDto result = service.runDemo(64, 10000);

        assertEquals(64, result.getThreads());
        assertEquals(10000, result.getIncrementsPerThread());
        assertEquals(640000, result.getExpected());
        assertTrue(result.getUnsafeCounter() < result.getExpected());
        assertEquals(result.getExpected(), result.getSynchronizedCounter());
        assertEquals(result.getExpected(), result.getAtomicCounter());
        assertTrue(result.isRaceConditionDetected());
    }

    @Test
    void runDemo_shouldWorkWithMinimumThreads() {
        RaceConditionDemoDto result = service.runDemo(50, 1000);

        assertEquals(50, result.getThreads());
        assertEquals(1000, result.getIncrementsPerThread());
        assertEquals(50000, result.getExpected());
        assertTrue(result.getUnsafeCounter() < result.getExpected());
        assertEquals(result.getExpected(), result.getSynchronizedCounter());
        assertEquals(result.getExpected(), result.getAtomicCounter());
        assertTrue(result.isRaceConditionDetected());
    }

    @Test
    void runDemo_shouldWorkWithLargeThreadCount() {
        RaceConditionDemoDto result = service.runDemo(100, 5000);

        assertEquals(100, result.getThreads());
        assertEquals(5000, result.getIncrementsPerThread());
        assertEquals(500000, result.getExpected());
        assertTrue(result.getUnsafeCounter() < result.getExpected());
        assertEquals(result.getExpected(), result.getSynchronizedCounter());
        assertEquals(result.getExpected(), result.getAtomicCounter());
        assertTrue(result.isRaceConditionDetected());
    }

    @Test
    void runDemo_shouldRejectThreadsBelow50() {
        assertThrows(IllegalArgumentException.class, () -> service.runDemo(49, 1000));
        assertThrows(IllegalArgumentException.class, () -> service.runDemo(0, 1000));
        assertThrows(IllegalArgumentException.class, () -> service.runDemo(-1, 1000));
    }

    @Test
    void runDemo_shouldRejectNonPositiveIncrements() {
        assertThrows(IllegalArgumentException.class, () -> service.runDemo(50, 0));
        assertThrows(IllegalArgumentException.class, () -> service.runDemo(64, -1));
        assertThrows(IllegalArgumentException.class, () -> service.runDemo(75, -100));
    }

    @Test
    void runDemo_shouldHaveSynchronizedCounterEqualToExpected() {
        RaceConditionDemoDto result = service.runDemo(50, 5000);

        assertEquals(250000, result.getExpected());
        assertEquals(result.getExpected(), result.getSynchronizedCounter());
    }

    @Test
    void runDemo_shouldHaveAtomicCounterEqualToExpected() {
        RaceConditionDemoDto result = service.runDemo(50, 5000);

        assertEquals(250000, result.getExpected());
        assertEquals(result.getExpected(), result.getAtomicCounter());
    }

    @Test
    void runDemo_raceConditionDetected_shouldBeTrueOnlyWhenConditionsMet() {
        RaceConditionDemoDto result = service.runDemo(64, 10000);

        boolean expectedFlag = result.getUnsafeCounter() < result.getExpected()
                && result.getSynchronizedCounter() == result.getExpected()
                && result.getAtomicCounter() == result.getExpected();

        assertEquals(expectedFlag, result.isRaceConditionDetected());
    }

    @Test
    void runDemo_shouldReturnValidDtoStructure() {
        RaceConditionDemoDto result = service.runDemo(60, 100);

        assertTrue(result.getThreads() > 0);
        assertTrue(result.getIncrementsPerThread() > 0);
        assertTrue(result.getExpected() > 0);
        assertTrue(result.getUnsafeCounter() >= 0);
        assertTrue(result.getSynchronizedCounter() >= 0);
        assertTrue(result.getAtomicCounter() >= 0);
    }

    @Test
    void runDemo_shouldReturnDtoWhenAwaitReturnsTrue() throws InterruptedException {
        RaceConditionDemoService spyService = spy(service);
        doReturn(true).when(spyService).awaitCompletedLatch(any(CountDownLatch.class));

        RaceConditionDemoDto result = spyService.runDemo(50, 100);

        assertEquals(50, result.getThreads());
        assertEquals(100, result.getIncrementsPerThread());
        assertEquals(5000, result.getExpected());
    }

    @Test
    void runDemo_shouldThrowTimeoutWhenNotCompleted() throws InterruptedException {
        RaceConditionDemoService spyService = spy(service);
        doReturn(false).when(spyService).awaitCompletedLatch(any(CountDownLatch.class));

        assertThrows(IllegalStateException.class, () -> spyService.runDemo(50, 100));
    }

    @Test
    void runDemo_shouldThrowInterruptedWhenAwaitInterrupted() throws InterruptedException {
        RaceConditionDemoService spyService = spy(service);
        doThrow(new InterruptedException("Test interrupt"))
                .when(spyService)
                .awaitCompletedLatch(any(CountDownLatch.class));

        assertThrows(IllegalStateException.class, () -> spyService.runDemo(50, 100));
    }

    @Test
    void isRaceConditionDetected_shouldReturnTrueWhenOnlyUnsafeDiffers() {
        assertTrue(service.isRaceConditionDetected(100, 90, 100, 100));
    }

    @Test
    void isRaceConditionDetected_shouldReturnFalseWhenSynchronizedDiffers() {
        assertFalse(service.isRaceConditionDetected(100, 90, 99, 100));
    }

    @Test
    void isRaceConditionDetected_shouldReturnFalseWhenAtomicDiffers() {
        assertFalse(service.isRaceConditionDetected(100, 90, 100, 99));
    }

    @Test
    void isRaceConditionDetected_shouldReturnFalseWhenUnsafeEqualsExpected() {
        assertFalse(service.isRaceConditionDetected(100, 100, 100, 100));
    }

    @Test
    void isRaceConditionDetected_shouldReturnFalseWhenAllMatch() {
        assertFalse(service.isRaceConditionDetected(100, 100, 100, 100));
    }

    @Test
    void isRaceConditionDetected_shouldReturnFalseWhenMultipleDiffer() {
        assertFalse(service.isRaceConditionDetected(100, 90, 90, 90));
    }
}
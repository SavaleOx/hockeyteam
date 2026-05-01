package com.savaleox.hockeyteam.exceptions;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class PartialBulkCreationException extends RuntimeException {

    private final int successCount;
    private final int failureCount;
    private final Map<Integer, String> failures;
    private final transient List<?> successfulResults;

    public PartialBulkCreationException(
            String message,
            int successCount,
            int failureCount,
            Map<Integer, String> failures,
            List<?> successfulResults
    ) {
        super(message);
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.failures = failures;
        this.successfulResults = successfulResults;
    }

    public int getCreatedPlayers() {
        return successCount;
    }
}
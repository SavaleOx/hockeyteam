package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard error response format")
public class ErrorResponseDto {

    @Schema(description = "Timestamp of the error", example = "2026-04-24T10:30:45.123")
    private String timestamp;

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error type", example = "Validation Failed")
    private String error;

    @Schema(description = "Human-readable error message", example = "Invalid input data. Please check the errors.")
    private String message;

    @Schema(description = "Request path that caused the error", example = "/players")
    private String path;

    @Schema(description = "Detailed validation errors per field")
    private Map<String, String> validationErrors;
}
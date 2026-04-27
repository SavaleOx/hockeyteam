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

    @Schema(description = "Время ошибки", example = "2026-04-24T10:30:45.123")
    private String timestamp;

    @Schema(description = "HTTP статус ошибки", example = "409")
    private int status;

    @Schema(description = "Тип ошибки", example = "Conflict")
    private String error;

    @Schema(description = "Сообщение об ошибке", example = "Неверный ввод данных.")
    private String message;

    @Schema(description = "Путь возникновения ошибки", example = "/players")
    private String path;

    @Schema(description = "Detailed validation errors per field")
    private Map<String, String> validationErrors;
}
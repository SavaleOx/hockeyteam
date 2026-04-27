package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request DTO for creating/updating player statistics")
public class StatisticRequestDto {

    @NotNull(message = "Player ID is required")
    @Positive(message = "Player ID must be positive")
    @Schema(description = "Идентификатор игрока", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long playerId;

    @NotNull(message = "Season is required")
    @Min(value = 2000, message = "Season must be 2000 or later")
    @Max(value = 2030, message = "Season cannot exceed 2026")
    @Schema(description = "Определённый сезон", example = "2026", minimum = "2000", maximum = "2026")
    private Integer season;

    @NotNull(message = "Goals are required")
    @Min(value = 0, message = "Goals cannot be negative")
    @Max(value = 200, message = "Goals cannot exceed 100")
    @Schema(description = "Всего забитых шайб за сезон", example = "15", minimum = "0", maximum = "100")
    private Integer goals;

    @NotNull(message = "Assists are required")
    @Min(value = 0, message = "Assists cannot be negative")
    @Max(value = 200, message = "Assists cannot exceed 100")
    @Schema(description = "Всего отданных ассистов за сезон", example = "25", minimum = "0", maximum = "100")
    private Integer assists;

    @NotNull(message = "Games are required")
    @Min(value = 0, message = "Games cannot be negative")
    @Max(value = 100, message = "Games cannot exceed 100")
    @Schema(description = "Количество сыгранных игр за сезон", example = "70", minimum = "0", maximum = "100")
    private Integer games;
}
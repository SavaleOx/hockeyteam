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
    @Schema(description = "ID of the player", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long playerId;

    @NotNull(message = "Season is required")
    @Min(value = 2000, message = "Season must be 2000 or later")
    @Max(value = 2030, message = "Season cannot exceed 2030")
    @Schema(description = "Hockey season year", example = "2023", minimum = "2000", maximum = "2030")
    private Integer season;

    @NotNull(message = "Goals are required")
    @Min(value = 0, message = "Goals cannot be negative")
    @Max(value = 200, message = "Goals cannot exceed 200")
    @Schema(description = "Total goals scored in the season", example = "15", minimum = "0", maximum = "200")
    private Integer goals;

    @NotNull(message = "Assists are required")
    @Min(value = 0, message = "Assists cannot be negative")
    @Max(value = 200, message = "Assists cannot exceed 200")
    @Schema(description = "Total assists made in the season", example = "25", minimum = "0", maximum = "200")
    private Integer assists;

    @NotNull(message = "Games are required")
    @Min(value = 0, message = "Games cannot be negative")
    @Max(value = 82, message = "Games cannot exceed 82 (regular season)")
    @Schema(description = "Number of games played", example = "70", minimum = "0", maximum = "82")
    private Integer games;
}
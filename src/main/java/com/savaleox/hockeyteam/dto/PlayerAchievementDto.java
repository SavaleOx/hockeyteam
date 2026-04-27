package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO for managing player achievements")
public class PlayerAchievementDto {

    @NotNull(message = "Player ID is required")
    @Positive(message = "Player ID must be positive")
    @Schema(description = "Идентификатор игрока", example = "1")
    private Long playerId;

    @NotNull(message = "Achievement ID is required")
    @Positive(message = "Achievement ID must be positive")
    @Schema(description = "Идентификатор достижения", example = "2")
    private Long achievementId;
}
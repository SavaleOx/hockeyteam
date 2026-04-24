package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Response DTO for player statistics")
public class StatisticResponseDto {

    @Schema(description = "Unique identifier of the statistic record", example = "1")
    private Long id;

    @Schema(description = "ID of the player", example = "1")
    private Long playerId;

    @Schema(description = "Full name of the player", example = "Connor McDavid")
    private String playerName;

    @Schema(description = "Hockey season year", example = "2023")
    private Integer season;

    @Schema(description = "Total goals scored in the season", example = "15")
    private Integer goals;

    @Schema(description = "Total assists made in the season", example = "25")
    private Integer assists;

    @Schema(description = "Number of games played", example = "70")
    private Integer games;

    @Schema(description = "Total points (goals + assists) in the season", example = "40")
    private Integer points;
}
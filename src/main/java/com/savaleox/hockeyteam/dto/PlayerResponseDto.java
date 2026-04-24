package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Response DTO for player data")
public class PlayerResponseDto {

    @Schema(description = "Unique identifier of the player", example = "1")
    private Long id;

    @Schema(description = "Full name of the player (name + surname)", example = "Connor McDavid")
    private String fullName;

    @Schema(description = "Player's jersey number", example = "97")
    private Integer number;

    @Schema(description = "Age of the player in years", example = "25")
    private Integer age;

    @Schema(description = "Total goals scored in the current season", example = "15")
    private Integer goals;

    @Schema(description = "Total assists made in the current season", example = "25")
    private Integer assists;

    @Schema(description = "Total points (goals + assists) in the current season", example = "40")
    private Integer points;

    @Schema(description = "Name of the team the player belongs to", example = "Victoria Maple Leafs")
    private String teamName;

    @Schema(description = "Playing position", example = "FORWARD")
    private String positionName;
}
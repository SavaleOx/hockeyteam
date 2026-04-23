package com.savaleox.hockeyteam.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode
@Schema(description = "Search criteria for players")
public class PlayerSearchCriteria {

    @Schema(example = "1")
    private Long playerId;

    @Schema(example = "19")
    private Integer playerAge;

    @Schema(example = "33")
    private Integer playerNumber;

    @Schema(example = "Victoria")
    private String teamName;

    @Schema(example = "FORWARD")
    private String playerPosition;

    @Schema(example = "5")
    private Integer minGoals;

    @Schema(example = "15")
    private Integer maxGoals;

    @Schema(example = "5")
    private Integer minAssists;

    @Schema(example = "15")
    private Integer maxAssists;

}

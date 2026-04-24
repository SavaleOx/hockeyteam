package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Schema(description = "Search criteria for filtering players")
public class PlayerSearchCriteria {

    @Schema(description = "Filter by player ID", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long playerId;

    @Schema(description = "Filter by player age", example = "25", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer playerAge;

    @Schema(description = "Filter by jersey number", example = "97", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer playerNumber;

    @Schema(description = "Filter by team name", example = "Victoria Maple Leafs",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String teamName;

    @Schema(description = "Filter by playing position", example = "FORWARD",
            allowableValues = {"GOALKEEPER", "DEFENDER", "FORWARD"})
    private String playerPosition;

    @Schema(description = "Minimum goals scored", example = "10", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer minGoals;

    @Schema(description = "Maximum goals scored", example = "30", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer maxGoals;

    @Schema(description = "Minimum assists made", example = "15", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer minAssists;

    @Schema(description = "Maximum assists made", example = "40", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer maxAssists;
}
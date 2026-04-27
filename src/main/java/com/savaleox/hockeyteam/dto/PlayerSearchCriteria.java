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

    @Schema(description = "Фильтр конкретного id игрока", example = "1",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long playerId;

    @Schema(description = "Фильтрация по конкретному возрасту", example = "25",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer playerAge;

    @Schema(description = "Фильтрация по конкретног=му игровому номеру", example = "97",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer playerNumber;

    @Schema(description = "Фильтрация по определённой команде", example = "Victoria Maple Leafs",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String teamName;

    @Schema(description = "Фильтрация по игровой позиции", example = "FORWARD",
            allowableValues = {"GOALKEEPER", "DEFENDER", "FORWARD"})
    private String playerPosition;

    @Schema(description = "Фильтрация по минимуму забитых шайб", example = "10",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer minGoals;

    @Schema(description = "Фильтрация по смаксимуму забитых шайб", example = "30",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer maxGoals;

    @Schema(description = "Фильтрация по минимальному количеству отданных ассистов",
            example = "15", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer minAssists;

    @Schema(description = "Фильтрация по максимальному количеству отданных ассистов",
            example = "40", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer maxAssists;
}
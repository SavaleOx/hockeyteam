package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Response DTO for player statistics")
public class StatisticResponseDto {

    @Schema(description = "Идентификатор статистической записи", example = "1")
    private Long id;

    @Schema(description = "Идентификатор игрока", example = "1")
    private Long playerId;

    @Schema(description = "Полное имя игрока", example = "Виталий Пинчук")
    private String playerName;

    @Schema(description = "Сезон", example = "2026")
    private Integer season;

    @Schema(description = "Суммарное количество забитых за сезон шайб", example = "15")
    private Integer goals;

    @Schema(description = "Суммарное количество отданных за сезон ассистов", example = "25")
    private Integer assists;

    @Schema(description = "Количество проведённых игр за сезон", example = "70")
    private Integer games;

    @Schema(description = "Количество очков за сезон (шайбы + ассисты)", example = "40")
    private Integer points;
}
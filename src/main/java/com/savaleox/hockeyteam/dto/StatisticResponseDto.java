package com.savaleox.hockeyteam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticResponseDto {
    private Long id;
    private Long playerId;
    private String playerName;
    private Integer season;
    private Integer goals;
    private Integer assists;
    private Integer games;
    private Integer points;
}
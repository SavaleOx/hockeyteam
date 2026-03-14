package com.savaleox.hockeyteam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatisticRequestDto {
    private Long playerId;
    private Integer season;
    private Integer goals;
    private Integer assists;
    private Integer games;
}
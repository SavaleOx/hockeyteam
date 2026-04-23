package com.savaleox.hockeyteam.dto;

import com.savaleox.hockeyteam.model.entity.Achievement;
import com.savaleox.hockeyteam.model.entity.Statistic;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class PlayerResponseDto {
    private Long id;
    private String fullName;
    private Integer number;
    private Integer age;
    private Integer goals;
    private Integer assists;
    private Integer points;
    private String teamName;
    private String positionName;
}
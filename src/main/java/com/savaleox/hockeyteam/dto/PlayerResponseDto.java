package com.savaleox.hockeyteam.dto;

import lombok.Getter;
import lombok.Setter;


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
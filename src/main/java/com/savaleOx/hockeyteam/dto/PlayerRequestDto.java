package com.savaleox.hockeyteam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerRequestDto {
    private String name;
    private String surname;
    private Integer number;
    private Integer age;
    private Long teamId;
    private Long positionId;
    private Integer goals;
    private Integer assists;
}
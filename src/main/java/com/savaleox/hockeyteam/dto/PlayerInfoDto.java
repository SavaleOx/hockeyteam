package com.savaleox.hockeyteam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerInfoDto {
    private Long id;
    private String fullName;
    private Integer number;
    private String teamName;
    private Integer goals;
    private Integer assists;

}
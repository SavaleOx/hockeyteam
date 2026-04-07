package com.savaleox.hockeyteam.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoachRequestDto {
    private String name;
    private String surname;
    private Integer age;
    private String tactic;
    private Long teamId;
}
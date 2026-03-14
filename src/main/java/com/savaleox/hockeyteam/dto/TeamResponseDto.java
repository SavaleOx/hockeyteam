package com.savaleox.hockeyteam.dto;


import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Getter
@Setter
public class TeamResponseDto {
    private Long id;
    private String name;
    private String city;
    private List<Long> playerIds;
}
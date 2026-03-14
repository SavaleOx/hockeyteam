package com.savaleox.hockeyteam.dto;

import java.util.List;

public class TeamResponseDto {
    private Long id;
    private String name;
    private String city;
    private List<Long> playerIds;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public List<Long> getPlayerIds() { return playerIds; }
    public void setPlayerIds(List<Long> playerIds) { this.playerIds = playerIds; }
}
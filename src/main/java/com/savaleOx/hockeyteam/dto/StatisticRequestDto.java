package com.savaleOx.hockeyteam.dto;

public class StatisticRequestDto {
    private Long playerId;
    private Integer season;
    private Integer goals;
    private Integer assists;
    private Integer games;

    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    public Integer getSeason() { return season; }
    public void setSeason(Integer season) { this.season = season; }
    public Integer getGoals() { return goals; }
    public void setGoals(Integer goals) { this.goals = goals; }
    public Integer getAssists() { return assists; }
    public void setAssists(Integer assists) { this.assists = assists; }
    public Integer getGames() { return games; }
    public void setGames(Integer games) { this.games = games; }
}
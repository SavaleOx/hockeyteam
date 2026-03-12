package com.savaleOx.hockeyteam.dto;

public class StatisticResponseDto {
    private Long id;
    private Long playerId;
    private String playerName;
    private Integer season;
    private Integer goals;
    private Integer assists;
    private Integer games;
    private Integer points;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPlayerId() { return playerId; }
    public void setPlayerId(Long playerId) { this.playerId = playerId; }
    public String getPlayerName() { return playerName; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }
    public Integer getSeason() { return season; }
    public void setSeason(Integer season) { this.season = season; }
    public Integer getGoals() { return goals; }
    public void setGoals(Integer goals) { this.goals = goals; }
    public Integer getAssists() { return assists; }
    public void setAssists(Integer assists) { this.assists = assists; }
    public Integer getGames() { return games; }
    public void setGames(Integer games) { this.games = games; }
    public Integer getPoints() { return points; }
    public void setPoints(Integer points) { this.points = points; }
}
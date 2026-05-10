package com.savaleox.hockeyteam.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AchievementPlayersDto {
    private List<PlayerInfoDto> playersWithAchievement;
    private List<PlayerInfoDto> playersWithoutAchievement;



    public AchievementPlayersDto(List<PlayerInfoDto> playersWithAchievement, List<PlayerInfoDto> playersWithoutAchievement) {
        this.playersWithAchievement = playersWithAchievement;
        this.playersWithoutAchievement = playersWithoutAchievement;
    }
}
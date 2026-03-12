package com.savaleOx.hockeyteam.mapper;

import com.savaleOx.hockeyteam.dto.AchievementRequestDto;
import com.savaleOx.hockeyteam.dto.AchievementResponseDto;
import com.savaleOx.hockeyteam.model.entity.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AchievementMapper {

    AchievementResponseDto toResponseDto(Achievement achievement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "players", ignore = true)
    Achievement toEntity(AchievementRequestDto dto);
}
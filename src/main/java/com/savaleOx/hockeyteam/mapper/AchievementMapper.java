package com.savaleox.hockeyteam.mapper;

import com.savaleox.hockeyteam.dto.AchievementRequestDto;
import com.savaleox.hockeyteam.dto.AchievementResponseDto;
import com.savaleox.hockeyteam.model.entity.Achievement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AchievementMapper {

    AchievementResponseDto toResponseDto(Achievement achievement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "players", ignore = true)
    Achievement toEntity(AchievementRequestDto dto);
}
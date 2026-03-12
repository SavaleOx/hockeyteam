package com.savaleOx.hockeyteam.mapper;

import com.savaleOx.hockeyteam.dto.StatisticRequestDto;
import com.savaleOx.hockeyteam.dto.StatisticResponseDto;
import com.savaleOx.hockeyteam.model.entity.Statistic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PlayerMapper.class})
public interface StatisticMapper {

    @Mapping(target = "playerId", source = "player.id")
    @Mapping(target = "playerName", expression = "java(statistic.getPlayer().getName() + \" \" + statistic.getPlayer().getSurname())")
    @Mapping(target = "points", expression = "java(statistic.getGoals() + statistic.getAssists())")
    StatisticResponseDto toResponseDto(Statistic statistic);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "player", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Statistic toEntity(StatisticRequestDto dto);
}
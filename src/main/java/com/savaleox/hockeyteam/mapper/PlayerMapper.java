package com.savaleox.hockeyteam.mapper;

import com.savaleox.hockeyteam.dto.PlayerRequestDto;
import com.savaleox.hockeyteam.dto.PlayerResponseDto;
import com.savaleox.hockeyteam.model.entity.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TeamMapper.class, PositionMapper.class})
public interface PlayerMapper {

    @Mapping(target = "fullName", expression = "java(player.getName() + \" \" + player.getSurname())")
    @Mapping(target = "points", expression = "java(player.getGoals() + player.getAssists())")
    @Mapping(target = "teamName", source = "team.name")
    @Mapping(target = "positionName", source = "position.name")
    PlayerResponseDto toResponseDto(Player player);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "statistics", ignore = true)
    @Mapping(target = "achievements", ignore = true)
    @Mapping(target = "goals", ignore = true)
    @Mapping(target = "assists", ignore = true)
    Player toEntity(PlayerRequestDto dto);
}
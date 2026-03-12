package com.savaleOx.hockeyteam.mapper;

import com.savaleOx.hockeyteam.dto.TeamRequestDto;
import com.savaleOx.hockeyteam.dto.TeamResponseDto;
import com.savaleOx.hockeyteam.model.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    @Mapping(target = "playerIds", expression = "java(team.getPlayers().stream().map(p -> p.getId()).toList())")
    TeamResponseDto toResponseDto(Team team);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "players", ignore = true)
    Team toEntity(TeamRequestDto dto);
}
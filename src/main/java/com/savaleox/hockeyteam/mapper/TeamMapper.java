package com.savaleox.hockeyteam.mapper;

import com.savaleox.hockeyteam.dto.TeamRequestDto;
import com.savaleox.hockeyteam.dto.TeamResponseDto;
import com.savaleox.hockeyteam.model.entity.Team;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    @Mapping(target = "playerIds", expression = "java(team.getPlayers().stream().map(p -> p.getId()).toList())")
    @Mapping(target = "coachId", source = "coach.id")
    @Mapping(target = "coachFullName", expression = "java(team.getCoach() != null ? team.getCoach().getName()" +
            " + \" \" + team.getCoach().getSurname() : null)")
    TeamResponseDto toResponseDto(Team team);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "players", ignore = true)
    @Mapping(target = "coach", ignore = true)
    Team toEntity(TeamRequestDto dto);
}
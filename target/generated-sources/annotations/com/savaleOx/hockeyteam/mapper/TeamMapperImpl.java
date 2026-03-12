package com.savaleOx.hockeyteam.mapper;

import com.savaleOx.hockeyteam.dto.TeamRequestDto;
import com.savaleOx.hockeyteam.dto.TeamResponseDto;
import com.savaleOx.hockeyteam.model.entity.Team;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-11T22:57:52+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class TeamMapperImpl implements TeamMapper {

    @Override
    public TeamResponseDto toResponseDto(Team team) {
        if ( team == null ) {
            return null;
        }

        TeamResponseDto teamResponseDto = new TeamResponseDto();

        teamResponseDto.setId( team.getId() );
        teamResponseDto.setName( team.getName() );
        teamResponseDto.setCity( team.getCity() );

        teamResponseDto.setPlayerIds( team.getPlayers().stream().map(p -> p.getId()).toList() );

        return teamResponseDto;
    }

    @Override
    public Team toEntity(TeamRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Team team = new Team();

        team.setName( dto.getName() );
        team.setCity( dto.getCity() );

        return team;
    }
}

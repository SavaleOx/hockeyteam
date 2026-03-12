package com.savaleOx.hockeyteam.mapper;

import com.savaleOx.hockeyteam.dto.PlayerRequestDto;
import com.savaleOx.hockeyteam.dto.PlayerResponseDto;
import com.savaleOx.hockeyteam.model.entity.Player;
import com.savaleOx.hockeyteam.model.entity.Position;
import com.savaleOx.hockeyteam.model.entity.Team;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-11T22:57:53+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class PlayerMapperImpl implements PlayerMapper {

    @Override
    public PlayerResponseDto toResponseDto(Player player) {
        if ( player == null ) {
            return null;
        }

        PlayerResponseDto playerResponseDto = new PlayerResponseDto();

        playerResponseDto.setTeamName( playerTeamName( player ) );
        playerResponseDto.setPositionName( playerPositionName( player ) );
        playerResponseDto.setId( player.getId() );
        playerResponseDto.setName( player.getName() );
        playerResponseDto.setSurname( player.getSurname() );
        playerResponseDto.setNumber( player.getNumber() );
        playerResponseDto.setAge( player.getAge() );
        playerResponseDto.setGoals( player.getGoals() );
        playerResponseDto.setAssists( player.getAssists() );

        playerResponseDto.setFullName( player.getName() + " " + player.getSurname() );
        playerResponseDto.setPoints( player.getGoals() + player.getAssists() );

        return playerResponseDto;
    }

    @Override
    public Player toEntity(PlayerRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Player player = new Player();

        player.setName( dto.getName() );
        player.setSurname( dto.getSurname() );
        player.setNumber( dto.getNumber() );
        player.setAge( dto.getAge() );

        return player;
    }

    private String playerTeamName(Player player) {
        if ( player == null ) {
            return null;
        }
        Team team = player.getTeam();
        if ( team == null ) {
            return null;
        }
        String name = team.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String playerPositionName(Player player) {
        if ( player == null ) {
            return null;
        }
        Position position = player.getPosition();
        if ( position == null ) {
            return null;
        }
        String name = position.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }
}

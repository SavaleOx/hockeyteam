package com.savaleOx.hockeyteam.mapper;

import com.savaleOx.hockeyteam.dto.StatisticRequestDto;
import com.savaleOx.hockeyteam.dto.StatisticResponseDto;
import com.savaleOx.hockeyteam.model.entity.Player;
import com.savaleOx.hockeyteam.model.entity.Statistic;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-11T22:57:53+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class StatisticMapperImpl implements StatisticMapper {

    @Override
    public StatisticResponseDto toResponseDto(Statistic statistic) {
        if ( statistic == null ) {
            return null;
        }

        StatisticResponseDto statisticResponseDto = new StatisticResponseDto();

        statisticResponseDto.setPlayerId( statisticPlayerId( statistic ) );
        statisticResponseDto.setId( statistic.getId() );
        statisticResponseDto.setSeason( statistic.getSeason() );
        statisticResponseDto.setGoals( statistic.getGoals() );
        statisticResponseDto.setAssists( statistic.getAssists() );
        statisticResponseDto.setGames( statistic.getGames() );

        statisticResponseDto.setPlayerName( statistic.getPlayer().getName() + " " + statistic.getPlayer().getSurname() );
        statisticResponseDto.setPoints( statistic.getGoals() + statistic.getAssists() );

        return statisticResponseDto;
    }

    @Override
    public Statistic toEntity(StatisticRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Statistic statistic = new Statistic();

        statistic.setSeason( dto.getSeason() );
        statistic.setGoals( dto.getGoals() );
        statistic.setAssists( dto.getAssists() );
        statistic.setGames( dto.getGames() );

        return statistic;
    }

    private Long statisticPlayerId(Statistic statistic) {
        if ( statistic == null ) {
            return null;
        }
        Player player = statistic.getPlayer();
        if ( player == null ) {
            return null;
        }
        Long id = player.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}

package com.savaleOx.hockeyteam.mapper;

import com.savaleOx.hockeyteam.dto.PositionRequestDto;
import com.savaleOx.hockeyteam.dto.PositionResponseDto;
import com.savaleOx.hockeyteam.model.entity.Position;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-11T22:57:53+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class PositionMapperImpl implements PositionMapper {

    @Override
    public PositionResponseDto toResponseDto(Position position) {
        if ( position == null ) {
            return null;
        }

        PositionResponseDto positionResponseDto = new PositionResponseDto();

        positionResponseDto.setId( position.getId() );
        positionResponseDto.setName( position.getName() );

        return positionResponseDto;
    }

    @Override
    public Position toEntity(PositionRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Position position = new Position();

        position.setName( dto.getName() );

        return position;
    }
}

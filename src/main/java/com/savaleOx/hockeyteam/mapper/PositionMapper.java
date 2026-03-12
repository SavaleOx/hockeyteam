package com.savaleOx.hockeyteam.mapper;

import com.savaleOx.hockeyteam.dto.PositionRequestDto;
import com.savaleOx.hockeyteam.dto.PositionResponseDto;
import com.savaleOx.hockeyteam.model.entity.Position;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PositionMapper {

    PositionResponseDto toResponseDto(Position position);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "players", ignore = true)
    Position toEntity(PositionRequestDto dto);
}
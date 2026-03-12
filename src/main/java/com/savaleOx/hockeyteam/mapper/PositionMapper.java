package com.savaleox.hockeyteam.mapper;

import com.savaleox.hockeyteam.dto.PositionRequestDto;
import com.savaleox.hockeyteam.dto.PositionResponseDto;
import com.savaleox.hockeyteam.model.entity.Position;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PositionMapper {

    PositionResponseDto toResponseDto(Position position);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "players", ignore = true)
    Position toEntity(PositionRequestDto dto);
}
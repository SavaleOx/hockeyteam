package com.savaleox.hockeyteam.mapper;

import com.savaleox.hockeyteam.dto.CoachRequestDto;
import com.savaleox.hockeyteam.dto.CoachResponseDto;
import com.savaleox.hockeyteam.model.entity.Coach;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TeamMapper.class})
public interface CoachMapper {

    @Mapping(target = "teamName", source = "team.name")
    CoachResponseDto toResponseDto(Coach coach);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "team", ignore = true)
    Coach toEntity(CoachRequestDto dto);
}
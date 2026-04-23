package com.savaleox.hockeyteam.mapper;

import com.savaleox.hockeyteam.dto.PlayerRequestDto;
import com.savaleox.hockeyteam.dto.PlayerResponseDto;
import com.savaleox.hockeyteam.model.entity.Player;
import com.savaleox.hockeyteam.model.enums.Position;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {TeamMapper.class})
public interface PlayerMapper {

    @Mapping(target = "fullName", expression = "java(player.getName() + \" \" + player.getSurname())")
    @Mapping(target = "points", expression = "java(player.getGoals() + player.getAssists())")
    @Mapping(target = "teamName", source = "team.name")
    @Mapping(target = "positionName", source = "position", qualifiedByName = "enumToString")
    PlayerResponseDto toResponseDto(Player player);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "statistics", ignore = true)
    @Mapping(target = "achievements", ignore = true)
    @Mapping(target = "goals", ignore = true)
    @Mapping(target = "assists", ignore = true)
    @Mapping(target = "position", source = "position", qualifiedByName = "stringToEnum")
    Player toEntity(PlayerRequestDto dto);

    @Named("enumToString")
    default String enumToString(Position position) {
        return position == null ? null : position.name();
    }

    @Named("stringToEnum")
    default Position stringToEnum(String position) {
        if (position == null) {
            return null;
        }
        try {
            return Position.valueOf(position.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
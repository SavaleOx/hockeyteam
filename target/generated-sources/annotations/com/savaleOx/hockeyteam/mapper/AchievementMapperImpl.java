package com.savaleOx.hockeyteam.mapper;

import com.savaleOx.hockeyteam.dto.AchievementRequestDto;
import com.savaleOx.hockeyteam.dto.AchievementResponseDto;
import com.savaleOx.hockeyteam.model.entity.Achievement;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-11T22:57:53+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 25.0.2 (Oracle Corporation)"
)
@Component
public class AchievementMapperImpl implements AchievementMapper {

    @Override
    public AchievementResponseDto toResponseDto(Achievement achievement) {
        if ( achievement == null ) {
            return null;
        }

        AchievementResponseDto achievementResponseDto = new AchievementResponseDto();

        achievementResponseDto.setId( achievement.getId() );
        achievementResponseDto.setName( achievement.getName() );
        achievementResponseDto.setDescription( achievement.getDescription() );

        return achievementResponseDto;
    }

    @Override
    public Achievement toEntity(AchievementRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        Achievement achievement = new Achievement();

        achievement.setName( dto.getName() );
        achievement.setDescription( dto.getDescription() );

        return achievement;
    }
}

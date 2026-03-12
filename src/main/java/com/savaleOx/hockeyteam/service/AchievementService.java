package com.savaleOx.hockeyteam.service;

import com.savaleOx.hockeyteam.dto.AchievementRequestDto;
import com.savaleOx.hockeyteam.dto.AchievementResponseDto;
import com.savaleOx.hockeyteam.mapper.AchievementMapper;
import com.savaleOx.hockeyteam.model.entity.Achievement;
import com.savaleOx.hockeyteam.repository.AchievementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;

    public AchievementService(AchievementRepository achievementRepository, AchievementMapper achievementMapper) {
        this.achievementRepository = achievementRepository;
        this.achievementMapper = achievementMapper;
    }

    public List<AchievementResponseDto> getAll() {
        return achievementRepository.findAll().stream()
                .map(achievementMapper::toResponseDto)
                .toList();
    }

    public AchievementResponseDto getById(Long id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Achievement not found"));
        return achievementMapper.toResponseDto(achievement);
    }

    public AchievementResponseDto getByName(String name) {
        Achievement achievement = achievementRepository.findByName(name);
        if (achievement == null) {
            throw new RuntimeException("Achievement not found");
        }
        return achievementMapper.toResponseDto(achievement);
    }

    @Transactional
    public AchievementResponseDto create(AchievementRequestDto dto) {
        Achievement achievement = achievementMapper.toEntity(dto);
        Achievement saved = achievementRepository.save(achievement);
        return achievementMapper.toResponseDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!achievementRepository.existsById(id)) {
            throw new RuntimeException("Achievement not found");
        }
        achievementRepository.deleteById(id);
    }
}
package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.AchievementRequestDto;
import com.savaleox.hockeyteam.dto.AchievementResponseDto;
import com.savaleox.hockeyteam.mapper.AchievementMapper;
import com.savaleox.hockeyteam.model.entity.Achievement;
import com.savaleox.hockeyteam.repository.AchievementRepository;
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

        achievementRepository.deleteById(id);
    }

    @Transactional
    public AchievementResponseDto update(Long id, AchievementRequestDto dto) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Achievement not found"));
        achievement.setName(dto.getName());
        achievement.setDescription(dto.getDescription());
        Achievement saved = achievementRepository.save(achievement);
        return achievementMapper.toResponseDto(saved);
    }

    @Transactional
    public AchievementResponseDto patch(Long id, AchievementRequestDto dto) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Achievement not found"));
        if (dto.getName() != null) {
            achievement.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            achievement.setDescription(dto.getDescription());
        }
        Achievement saved = achievementRepository.save(achievement);
        return achievementMapper.toResponseDto(saved);
    }
}
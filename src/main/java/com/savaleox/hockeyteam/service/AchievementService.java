package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.AchievementPlayersDto;
import com.savaleox.hockeyteam.dto.AchievementRequestDto;
import com.savaleox.hockeyteam.dto.AchievementResponseDto;
import com.savaleox.hockeyteam.dto.PlayerInfoDto;
import com.savaleox.hockeyteam.mapper.AchievementMapper;
import com.savaleox.hockeyteam.model.entity.Achievement;
import com.savaleox.hockeyteam.model.entity.Player;
import com.savaleox.hockeyteam.repository.AchievementRepository;
import com.savaleox.hockeyteam.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;
    private final PlayerRepository playerRepository;
    private final PlayerService playerService;

    public AchievementService(AchievementRepository achievementRepository,
                              AchievementMapper achievementMapper,
                              PlayerRepository playerRepository,
                              PlayerService playerService) {
        this.achievementRepository = achievementRepository;
        this.achievementMapper = achievementMapper;
        this.playerRepository = playerRepository;
        this.playerService = playerService;
    }

    public List<AchievementResponseDto> getAll() {
        return achievementRepository.findAll().stream()
                .map(achievementMapper::toResponseDto)
                .toList();
    }

    public AchievementResponseDto getById(Long id) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow();
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
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Achievement not found"));

        // Удалить связи в таблице player_achievements
        playerRepository.deleteAchievementFromAllPlayers(id);

        // Удалить само достижение
        achievementRepository.delete(achievement);

        playerService.invalidateSearchCache();
    }

    @Transactional
    public AchievementResponseDto update(Long id, AchievementRequestDto dto) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow();
        achievement.setName(dto.getName());
        achievement.setDescription(dto.getDescription());
        Achievement saved = achievementRepository.save(achievement);
        return achievementMapper.toResponseDto(saved);
    }

    @Transactional
    public AchievementResponseDto patch(Long id, AchievementRequestDto dto) {
        Achievement achievement = achievementRepository.findById(id)
                .orElseThrow();
        if (dto.getName() != null) {
            achievement.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            achievement.setDescription(dto.getDescription());
        }
        Achievement saved = achievementRepository.save(achievement);
        return achievementMapper.toResponseDto(saved);
    }

    public AchievementPlayersDto getAchievementPlayers(Long achievementId) {
        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new RuntimeException("Achievement not found"));

        // Игроки, у которых есть это достижение
        List<PlayerInfoDto> playersWith = achievement.getPlayers().stream()
                .map(this::toPlayerInfoDto)
                .toList();

        // Все игроки, у которых НЕТ этого достижения
        List<PlayerInfoDto> playersWithout = playerRepository.findAll().stream()
                .filter(p -> !achievement.getPlayers().contains(p))
                .map(this::toPlayerInfoDto)
                .toList();

        return new AchievementPlayersDto(playersWith, playersWithout);
    }

    private PlayerInfoDto toPlayerInfoDto(Player player) {
        PlayerInfoDto dto = new PlayerInfoDto();
        dto.setId(player.getId());
        dto.setFullName(player.getName() + " " + player.getSurname());
        dto.setNumber(player.getNumber());
        dto.setTeamName(player.getTeam() != null ? player.getTeam().getName() : null);
        dto.setGoals(player.getGoals());
        dto.setAssists(player.getAssists());
        return dto;
    }
}
package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.cache.SearchCacheKey;
import com.savaleox.hockeyteam.cache.SearchCacheManager;
import com.savaleox.hockeyteam.dto.AchievementResponseDto;
import com.savaleox.hockeyteam.dto.PlayerRequestDto;
import com.savaleox.hockeyteam.dto.PlayerResponseDto;
import com.savaleox.hockeyteam.dto.PlayerSearchCriteria;
import com.savaleox.hockeyteam.mapper.AchievementMapper;
import com.savaleox.hockeyteam.mapper.PlayerMapper;
import com.savaleox.hockeyteam.model.entity.Achievement;
import com.savaleox.hockeyteam.model.entity.Player;
import com.savaleox.hockeyteam.model.entity.Team;
import com.savaleox.hockeyteam.model.enums.Position;
import com.savaleox.hockeyteam.repository.AchievementRepository;
import com.savaleox.hockeyteam.repository.PlayerRepository;
import com.savaleox.hockeyteam.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.savaleox.hockeyteam.exceptions.ResourceNotFoundException;

import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class PlayerService {
    private final SearchCacheManager cacheManager;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerMapper playerMapper;
    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;

    public PlayerService(SearchCacheManager cacheManager,
                         PlayerRepository playerRepository,
                         TeamRepository teamRepository,
                         PlayerMapper playerMapper,
                         AchievementRepository achievementRepository,
                         AchievementMapper achievementMapper) {
        this.cacheManager = cacheManager;
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.playerMapper = playerMapper;
        this.achievementRepository = achievementRepository;
        this.achievementMapper = achievementMapper;
    }

    public List<PlayerResponseDto> getAll() {
        return playerRepository.findAll().stream()
                .map(playerMapper::toResponseDto)
                .toList();
    }

    public List<PlayerResponseDto> getByTeam(Long teamId) {
        return playerRepository.findByTeamId(teamId).stream()
                .map(playerMapper::toResponseDto)
                .toList();
    }

    public List<PlayerResponseDto> getByPosition(String positionName) {
        Position position;
        try {
            position = Position.valueOf(positionName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return List.of();
        }
        return playerRepository.findByPosition(position).stream()
                .map(playerMapper::toResponseDto)
                .toList();
    }

    public List<PlayerResponseDto> getByTeamAndPosition(Long teamId, String positionName) {
        Position position;
        try {
            position = Position.valueOf(positionName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return List.of();
        }
        return playerRepository.findByTeamIdAndPosition(teamId, position).stream()
                .map(playerMapper::toResponseDto)
                .toList();
    }

    public List<PlayerResponseDto> getByMinGoals(Integer minGoals) {
        return playerRepository.findByMinGoals(minGoals).stream()
                .map(playerMapper::toResponseDto)
                .toList();
    }

    public PlayerResponseDto getById(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("", id));
        return playerMapper.toResponseDto(player);
    }

    @Transactional
    public PlayerResponseDto create(PlayerRequestDto dto) {
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team", dto.getTeamId()));

        Player player = playerMapper.toEntity(dto);
        player.setTeam(team);
        player.setGoals(dto.getGoals());
        player.setAssists(dto.getAssists());

        if (dto.getPosition() != null) {
            player.setPosition(Position.valueOf(dto.getPosition().toUpperCase()));
        }

        Player saved = playerRepository.save(player);
        invalidateSearchCache();
        return playerMapper.toResponseDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        playerRepository.deleteById(id);
        invalidateSearchCache();
    }

    @Transactional
    public PlayerResponseDto update(Long id, PlayerRequestDto dto) {
        Player player = playerRepository.findById(id)
                .orElseThrow();

        player.setName(dto.getName());
        player.setSurname(dto.getSurname());
        player.setNumber(dto.getNumber());
        player.setAge(dto.getAge());
        player.setGoals(dto.getGoals());
        player.setAssists(dto.getAssists());

        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow();
            player.setTeam(team);
        }

        if (dto.getPosition() != null) {
            player.setPosition(Position.valueOf(dto.getPosition().toUpperCase()));
        }

        Player saved = playerRepository.save(player);
        invalidateSearchCache();
        return playerMapper.toResponseDto(saved);
    }

    @Transactional
    public PlayerResponseDto patch(Long id, PlayerRequestDto dto) {
        Player player = playerRepository.findById(id)
                .orElseThrow();

        if (dto.getName() != null) {
            player.setName(dto.getName());
        }
        if (dto.getSurname() != null) {
            player.setSurname(dto.getSurname());
        }
        if (dto.getNumber() != null) {
            player.setNumber(dto.getNumber());
        }
        if (dto.getAge() != null) {
            player.setAge(dto.getAge());
        }
        if (dto.getGoals() != null) {
            player.setGoals(dto.getGoals());
        }
        if (dto.getAssists() != null) {
            player.setAssists(dto.getAssists());
        }

        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow();
            player.setTeam(team);
        }

        if (dto.getPosition() != null) {

            player.setPosition(Position.valueOf(dto.getPosition().toUpperCase()));
        }

        Player saved = playerRepository.save(player);
        invalidateSearchCache();
        return playerMapper.toResponseDto(saved);
    }

    public Page<PlayerResponseDto> searchPlayersJPQL(PlayerSearchCriteria criteria, Pageable pageable) {
        SearchCacheKey key = new SearchCacheKey(criteria, pageable);
        Page<PlayerResponseDto> cached = cacheManager.get(key);
        if (cached != null) {
            return cached;
        }
        Page<Player> playerPage = playerRepository.searchWithFiltersJPQL(criteria, pageable);
        Page<PlayerResponseDto> dtoPage = playerPage.map(playerMapper::toResponseDto);
        cacheManager.put(key, dtoPage);
        return dtoPage;
    }

    public Page<PlayerResponseDto> searchPlayersNative(PlayerSearchCriteria criteria, Pageable pageable) {
        SearchCacheKey key = new SearchCacheKey(criteria, pageable);
        Page<PlayerResponseDto> cached = cacheManager.get(key);
        if (cached != null) {
            return cached;
        }
        Page<Player> playerPage = playerRepository.searchWithFiltersNative(criteria, pageable);
        Page<PlayerResponseDto> dtoPage = playerPage.map(playerMapper::toResponseDto);
        cacheManager.put(key, dtoPage);
        return dtoPage;
    }

    public void invalidateSearchCache() {
        cacheManager.invalidateAll();
    }

    @Transactional
    public PlayerResponseDto addAchievement(Long playerId, Long achievementId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("", playerId));

        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement", achievementId));

        if (player.getAchievements().contains(achievement)) {
            throw new IllegalStateException(
                    String.format("Player %d already has achievement '%s'", playerId, achievement.getName())
            );
        }

        player.getAchievements().add(achievement);
        achievement.getPlayers().add(player);

        playerRepository.save(player);
        invalidateSearchCache();

        log.info("Added achievement '{}' to player {}", achievement.getName(), player.getId());
        return playerMapper.toResponseDto(player);
    }

    @Transactional
    public PlayerResponseDto removeAchievement(Long playerId, Long achievementId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("", playerId));

        Achievement achievement = achievementRepository.findById(achievementId)
                .orElseThrow(() -> new ResourceNotFoundException("Achievement", achievementId));

        if (!player.getAchievements().contains(achievement)) {
            throw new IllegalStateException(
                    String.format("Player %d doesn't have achievement '%s'", playerId, achievement.getName())
            );
        }

        player.getAchievements().remove(achievement);
        achievement.getPlayers().remove(player);

        playerRepository.save(player);
        invalidateSearchCache();

        log.info("Removed achievement '{}' from player {}", achievement.getName(), player.getId());
        return playerMapper.toResponseDto(player);
    }

    public List<AchievementResponseDto> getPlayerAchievements(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("", playerId));

        return player.getAchievements().stream()
                .map(achievementMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public PlayerResponseDto setAchievements(Long playerId, List<Long> achievementIds) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("", playerId));

        List<Achievement> newAchievements = achievementRepository.findAllById(achievementIds);

        if (newAchievements.size() != achievementIds.size()) {
            throw new IllegalArgumentException("One or more achievements not found");
        }

        for (Achievement achievement : player.getAchievements()) {
            achievement.getPlayers().remove(player);
        }

        player.setAchievements(new HashSet<>(newAchievements));
        for (Achievement achievement : newAchievements) {
            achievement.getPlayers().add(player);
        }

        playerRepository.save(player);
        invalidateSearchCache();

        log.info("Set {} achievements for player {}", newAchievements.size(), player.getId());
        return playerMapper.toResponseDto(player);
    }
}
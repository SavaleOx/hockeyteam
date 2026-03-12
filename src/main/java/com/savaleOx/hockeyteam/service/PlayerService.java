package com.savaleOx.hockeyteam.service;

import com.savaleOx.hockeyteam.dto.PlayerRequestDto;
import com.savaleOx.hockeyteam.dto.PlayerResponseDto;
import com.savaleOx.hockeyteam.mapper.PlayerMapper;
import com.savaleOx.hockeyteam.model.entity.Player;
import com.savaleOx.hockeyteam.model.entity.Team;
import com.savaleOx.hockeyteam.model.entity.Position;
import com.savaleOx.hockeyteam.repository.PlayerRepository;
import com.savaleOx.hockeyteam.repository.TeamRepository;
import com.savaleOx.hockeyteam.repository.PositionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PositionRepository positionRepository;
    private final PlayerMapper playerMapper;

    public PlayerService(PlayerRepository playerRepository, TeamRepository teamRepository,
                         PositionRepository positionRepository, PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.positionRepository = positionRepository;
        this.playerMapper = playerMapper;
    }

    public List<PlayerResponseDto> getAll() {
        return playerRepository.findAll().stream()
                .map(playerMapper::toResponseDto)
                .toList();
    }

    public PlayerResponseDto getById(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        return playerMapper.toResponseDto(player);
    }

    public List<PlayerResponseDto> getByTeam(Long teamId) {
        return playerRepository.findByTeamId(teamId).stream()
                .map(playerMapper::toResponseDto)
                .toList();
    }

    public List<PlayerResponseDto> getByPosition(String positionName) {
        return playerRepository.findByPositionName(positionName).stream()
                .map(playerMapper::toResponseDto)
                .toList();
    }

    public List<PlayerResponseDto> getByTeamAndPosition(Long teamId, String positionName) {
        return playerRepository.findByTeamIdAndPositionName(teamId, positionName).stream()
                .map(playerMapper::toResponseDto)
                .toList();
    }

    public List<PlayerResponseDto> getByMinGoals(Integer minGoals) {
        return playerRepository.findByMinGoals(minGoals).stream()
                .map(playerMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public PlayerResponseDto create(PlayerRequestDto dto) {
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));
        Position position = positionRepository.findById(dto.getPositionId())
                .orElseThrow(() -> new RuntimeException("Position not found"));

        Player player = playerMapper.toEntity(dto);
        player.setTeam(team);
        player.setPosition(position);
        player.setGoals(0);
        player.setAssists(0);

        Player saved = playerRepository.save(player);
        return playerMapper.toResponseDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!playerRepository.existsById(id)) {
            throw new RuntimeException("Player not found");
        }
        playerRepository.deleteById(id);
    }
}
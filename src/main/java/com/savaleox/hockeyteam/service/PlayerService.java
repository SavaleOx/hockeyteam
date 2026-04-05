package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.PlayerRequestDto;
import com.savaleox.hockeyteam.dto.PlayerResponseDto;
import com.savaleox.hockeyteam.mapper.PlayerMapper;
import com.savaleox.hockeyteam.model.entity.Player;
import com.savaleox.hockeyteam.model.entity.Team;
import com.savaleox.hockeyteam.model.entity.Position;
import com.savaleox.hockeyteam.repository.PlayerRepository;
import com.savaleox.hockeyteam.repository.TeamRepository;
import com.savaleox.hockeyteam.repository.PositionRepository;
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

    public PlayerResponseDto getById(Long id) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        return playerMapper.toResponseDto(player);
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
        player.setGoals(dto.getGoals());
        player.setAssists(dto.getAssists());

        Player saved = playerRepository.save(player);
        return playerMapper.toResponseDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        playerRepository.deleteById(id);
    }

    @Transactional
    public PlayerResponseDto update(Long id, PlayerRequestDto dto) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found"));
        player.setName(dto.getName());
        player.setSurname(dto.getSurname());
        player.setNumber(dto.getNumber());
        player.setAge(dto.getAge());
        player.setGoals(dto.getGoals());
        player.setAssists(dto.getAssists());
        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found"));
            player.setTeam(team);
        }
        if (dto.getPositionId() != null) {
            Position position = positionRepository.findById(dto.getPositionId())
                    .orElseThrow(() -> new RuntimeException("Position not found"));
            player.setPosition(position);
        }

        Player saved = playerRepository.save(player);
        return playerMapper.toResponseDto(saved);
    }

    @Transactional
    public PlayerResponseDto patch(Long id, PlayerRequestDto dto) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        if (dto.getName() != null) player.setName(dto.getName());
        if (dto.getSurname() != null) player.setSurname(dto.getSurname());
        if (dto.getNumber() != null) player.setNumber(dto.getNumber());
        if (dto.getAge() != null) player.setAge(dto.getAge());
        if (dto.getGoals() != null) player.setGoals(dto.getGoals());
        if (dto.getAssists() != null) player.setAssists(dto.getAssists());

        if (dto.getTeamId() != null) {
            Team team = teamRepository.findById(dto.getTeamId())
                    .orElseThrow(() -> new RuntimeException("Team not found"));
            player.setTeam(team);
        }
        if (dto.getPositionId() != null) {
            Position position = positionRepository.findById(dto.getPositionId())
                    .orElseThrow(() -> new RuntimeException("Position not found"));
            player.setPosition(position);
        }

        Player saved = playerRepository.save(player);
        return playerMapper.toResponseDto(saved);
    }
}
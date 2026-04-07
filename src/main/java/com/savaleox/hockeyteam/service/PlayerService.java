package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.PlayerRequestDto;
import com.savaleox.hockeyteam.dto.PlayerResponseDto;
import com.savaleox.hockeyteam.mapper.PlayerMapper;
import com.savaleox.hockeyteam.model.entity.Player;
import com.savaleox.hockeyteam.model.entity.Team;
import com.savaleox.hockeyteam.model.enums.Position;
import com.savaleox.hockeyteam.repository.PlayerRepository;
import com.savaleox.hockeyteam.repository.TeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PlayerMapper playerMapper;

    public PlayerService(PlayerRepository playerRepository,
                         TeamRepository teamRepository,
                         PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
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
                .orElseThrow(() -> new RuntimeException("Player not found"));
        return playerMapper.toResponseDto(player);
    }

    @Transactional
    public PlayerResponseDto create(PlayerRequestDto dto) {
        Team team = teamRepository.findById(dto.getTeamId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        Player player = playerMapper.toEntity(dto);
        player.setTeam(team);
        player.setGoals(dto.getGoals());
        player.setAssists(dto.getAssists());

        if (dto.getPosition() != null) {
            try {
                player.setPosition(Position.valueOf(dto.getPosition().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid position: " + dto.getPosition());
            }
        }

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

        if (dto.getPosition() != null) {
            try {
                player.setPosition(Position.valueOf(dto.getPosition().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid position: " + dto.getPosition());
            }
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

        if (dto.getPosition() != null) {
            try {
                player.setPosition(Position.valueOf(dto.getPosition().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Invalid position: " + dto.getPosition());
            }
        }

        Player saved = playerRepository.save(player);
        return playerMapper.toResponseDto(saved);
    }
}
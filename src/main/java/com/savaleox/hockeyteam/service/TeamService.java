package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.TeamRequestDto;
import com.savaleox.hockeyteam.dto.TeamResponseDto;
import com.savaleox.hockeyteam.mapper.TeamMapper;
import com.savaleox.hockeyteam.model.entity.Coach;
import com.savaleox.hockeyteam.model.entity.Team;
import com.savaleox.hockeyteam.repository.TeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final PlayerService playerService;

    public TeamService(TeamRepository teamRepository, TeamMapper teamMapper, PlayerService playerService) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.playerService = playerService;
    }

    public List<TeamResponseDto> getAll() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toResponseDto)
                .toList();
    }

    public TeamResponseDto getById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow();
        return teamMapper.toResponseDto(team);
    }

    @Transactional
    public TeamResponseDto create(TeamRequestDto dto) {
        Team team = teamMapper.toEntity(dto);
        Team saved = teamRepository.save(team);
        playerService.invalidateSearchCache();
        return teamMapper.toResponseDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        Team team = teamRepository.findById(id).orElseThrow();

        if (team.getCoach() != null) {
            Coach coach = team.getCoach();
            coach.setTeam(null);
        }
        playerService.invalidateSearchCache();
        teamRepository.deleteById(id);
    }

    @Transactional
    public TeamResponseDto update(Long id, TeamRequestDto dto) {
        Team team = teamRepository.findById(id)
                .orElseThrow();
        team.setName(dto.getName());
        team.setCity(dto.getCity());
        Team saved = teamRepository.save(team);
        playerService.invalidateSearchCache();
        return teamMapper.toResponseDto(saved);
    }

    @Transactional
    public TeamResponseDto patch(Long id, TeamRequestDto dto) {
        Team team = teamRepository.findById(id)
                .orElseThrow();
        if (dto.getName() != null) {
            team.setName(dto.getName());
        }
        if (dto.getCity() != null) {
            team.setCity(dto.getCity());
        }
        Team saved = teamRepository.save(team);
        playerService.invalidateSearchCache();
        return teamMapper.toResponseDto(saved);
    }
}
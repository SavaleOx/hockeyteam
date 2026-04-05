package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.TeamRequestDto;
import com.savaleox.hockeyteam.dto.TeamResponseDto;
import com.savaleox.hockeyteam.mapper.TeamMapper;
import com.savaleox.hockeyteam.model.entity.Team;
import com.savaleox.hockeyteam.repository.TeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public TeamService(TeamRepository teamRepository, TeamMapper teamMapper) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
    }

    public List<TeamResponseDto> getAll() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toResponseDto)
                .toList();
    }

    public TeamResponseDto getById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        return teamMapper.toResponseDto(team);
    }

    @Transactional
    public TeamResponseDto create(TeamRequestDto dto) {
        Team team = teamMapper.toEntity(dto);
        Team saved = teamRepository.save(team);
        return teamMapper.toResponseDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        teamRepository.deleteById(id);
    }

    @Transactional
    public TeamResponseDto update(Long id, TeamRequestDto dto) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        team.setName(dto.getName());
        team.setCity(dto.getCity());
        Team saved = teamRepository.save(team);
        return teamMapper.toResponseDto(saved);
    }

    @Transactional
    public TeamResponseDto patch(Long id, TeamRequestDto dto) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        if (dto.getName() != null) {
            team.setName(dto.getName());
        }
        if (dto.getCity() != null) {
            team.setCity(dto.getCity());
        }
        Team saved = teamRepository.save(team);
        return teamMapper.toResponseDto(saved);
    }
}
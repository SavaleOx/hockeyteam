package com.savaleOx.hockeyteam.service;

import com.savaleOx.hockeyteam.dto.TeamRequestDto;
import com.savaleOx.hockeyteam.dto.TeamResponseDto;
import com.savaleOx.hockeyteam.mapper.TeamMapper;
import com.savaleOx.hockeyteam.model.entity.Team;
import com.savaleOx.hockeyteam.repository.TeamRepository;
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
        if (!teamRepository.existsById(id)) {
            throw new RuntimeException("Team not found");
        }
        teamRepository.deleteById(id);
    }
}
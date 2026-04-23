package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.CoachRequestDto;
import com.savaleox.hockeyteam.dto.CoachResponseDto;
import com.savaleox.hockeyteam.mapper.CoachMapper;
import com.savaleox.hockeyteam.model.entity.Coach;
import com.savaleox.hockeyteam.model.entity.Team;
import com.savaleox.hockeyteam.repository.CoachRepository;
import com.savaleox.hockeyteam.repository.TeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoachService {

    private final CoachRepository coachRepository;
    private final TeamRepository teamRepository;
    private final CoachMapper coachMapper;

    public CoachService(CoachRepository coachRepository,
                        TeamRepository teamRepository,
                        CoachMapper coachMapper) {
        this.coachRepository = coachRepository;
        this.teamRepository = teamRepository;
        this.coachMapper = coachMapper;
    }

    public List<CoachResponseDto> getAll() {
        return coachRepository.findAll().stream()
                .map(coachMapper::toResponseDto)
                .toList();
    }

    public CoachResponseDto getById(Long id) {
        Coach coach = coachRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coach not found"));
        return coachMapper.toResponseDto(coach);
    }

    public List<CoachResponseDto> getByTeam(Long teamId) {
        return coachRepository.findByTeamId(teamId).stream()
                .map(coachMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public CoachResponseDto create(CoachRequestDto dto) {
        Team team = null;
        Coach coach = coachMapper.toEntity(dto);
        coach.setTeam(teamRepository.getById(dto.getTeamId()));
        coach.setAge(dto.getAge());
        coach.setTactic(dto.getTactic());
        Coach saved = coachRepository.save(coach);
        return coachMapper.toResponseDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        Coach coach = coachRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Coach not found"));
        if (coach.getTeam() != null) {
            Team team = coach.getTeam();
            team.setCoach(null);
            teamRepository.save(team);
        }
        coachRepository.deleteById(id);
    }

    @Transactional
    public CoachResponseDto update(Long id, CoachRequestDto dto) {
        Coach coach = coachRepository.findById(id)
                .orElseThrow();
        coach.setName(dto.getName());
        coach.setSurname(dto.getSurname());
        if (dto.getAge() != null) coach.setAge(dto.getAge());
        if (dto.getTactic() != null) coach.setTactic(dto.getTactic());

        Long newTeamId = dto.getTeamId();
        if (newTeamId != null) {
            Team newTeam = teamRepository.findById(newTeamId)
                    .orElseThrow();
            if (coach.getTeam() != null && !coach.getTeam().getId().equals(newTeamId)) {
                Team oldTeam = coach.getTeam();
                oldTeam.setCoach(null);
                teamRepository.save(oldTeam);
            }

            coach.setTeam(newTeam);
            newTeam.setCoach(coach);
            teamRepository.save(newTeam);
        } else {
            if (coach.getTeam() != null) {
                Team oldTeam = coach.getTeam();
                oldTeam.setCoach(null);
                teamRepository.save(oldTeam);
                coach.setTeam(null);
            }
        }

        Coach saved = coachRepository.save(coach);
        return coachMapper.toResponseDto(saved);
    }

    @Transactional
    public CoachResponseDto patch(Long id, CoachRequestDto dto) {
        Coach coach = coachRepository.findById(id)
                .orElseThrow();

        if (dto.getName() != null) coach.setName(dto.getName());
        if (dto.getSurname() != null) coach.setSurname(dto.getSurname());
        if (dto.getAge() != null) coach.setAge(dto.getAge());
        if (dto.getTactic() != null) coach.setTactic(dto.getTactic());

        if (dto.getTeamId() != null) {
            Team newTeam = teamRepository.findById(dto.getTeamId())
                    .orElseThrow();
            if (coach.getTeam() != null && !coach.getTeam().getId().equals(newTeam.getId())) {
                Team oldTeam = coach.getTeam();
                oldTeam.setCoach(null);
                teamRepository.save(oldTeam);
            }

            coach.setTeam(newTeam);
            newTeam.setCoach(coach);
            teamRepository.save(newTeam);
        }

        Coach saved = coachRepository.save(coach);
        return coachMapper.toResponseDto(saved);
    }
}

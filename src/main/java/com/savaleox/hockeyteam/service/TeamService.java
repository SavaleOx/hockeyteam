package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.PlayerRequestDto;
import com.savaleox.hockeyteam.dto.PlayerResponseDto;
import com.savaleox.hockeyteam.dto.TeamRequestDto;
import com.savaleox.hockeyteam.dto.TeamResponseDto;
import com.savaleox.hockeyteam.exceptions.PartialBulkCreationException;
import com.savaleox.hockeyteam.mapper.PlayerMapper;
import com.savaleox.hockeyteam.mapper.TeamMapper;
import com.savaleox.hockeyteam.model.entity.Player;
import com.savaleox.hockeyteam.model.entity.Team;
import com.savaleox.hockeyteam.model.enums.Position;
import com.savaleox.hockeyteam.repository.PlayerRepository;
import com.savaleox.hockeyteam.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final PlayerService playerService;
    private final PlayerMapper playerMapper;
    private final PlayerRepository playerRepository;

    public TeamService(TeamRepository teamRepository,
                       TeamMapper teamMapper,
                       PlayerService playerService,
                       PlayerMapper playerMapper,
                       PlayerRepository playerRepository) {
        this.teamRepository = teamRepository;
        this.teamMapper = teamMapper;
        this.playerService = playerService;
        this.playerMapper = playerMapper;
        this.playerRepository = playerRepository;
    }

    public List<TeamResponseDto> getAll() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toResponseDto)
                .toList();
    }

    public TeamResponseDto getById(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Team with id " + id + " not found"));
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

    @Transactional
    public List<PlayerResponseDto> bulkCreatePlayers(Long teamId, List<PlayerRequestDto> players) {

        List<PlayerRequestDto> safePlayers = Optional.ofNullable(players)
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("Players list cannot be empty"));

        log.info("Starting TRANSACTIONAL bulk player creation for teamId={}, size={}", teamId, safePlayers.size());

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team with id " + teamId + " not found"));

        Set<Integer> existingNumbers = team.getPlayers().stream()
                .map(Player::getNumber)
                .collect(Collectors.toSet());

        List<Player> playersToSave = new ArrayList<>();

        for (int i = 0; i < safePlayers.size(); i++) {
            PlayerRequestDto dto = safePlayers.get(i);
            validatePlayerForTeam(dto, existingNumbers, playersToSave);

            Player player = playerMapper.toEntity(dto);
            player.setTeam(team);
            player.setPosition(Position.valueOf(dto.getPosition().toUpperCase()));
            player.setGoals(dto.getGoals());
            player.setAssists(dto.getAssists());
            playersToSave.add(player);
        }

        List<Player> savedPlayers = playerRepository.saveAll(playersToSave);
        playerService.invalidateSearchCache();

        List<PlayerResponseDto> result = savedPlayers.stream()
                .map(playerMapper::toResponseDto)
                .toList();

        log.info("Transactional bulk creation completed: {} players created", result.size());
        return result;
    }

    public List<PlayerResponseDto> bulkCreatePlayersWithoutTransaction(Long teamId, List<PlayerRequestDto> players) {

        List<PlayerRequestDto> safePlayers = Optional.ofNullable(players)
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("Players list cannot be empty"));

        log.info("Starting NON-TRANSACTIONAL bulk player creation for teamId={}, size={}", teamId, safePlayers.size());

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team with id " + teamId + " not found"));

        Set<Integer> existingNumbers = team.getPlayers().stream()
                .map(Player::getNumber)
                .collect(Collectors.toSet());

        List<PlayerResponseDto> createdPlayers = new ArrayList<>();
        Map<Integer, String> failures = new LinkedHashMap<>();
        Set<Integer> usedNumbersInBatch = new HashSet<>();

        for (int i = 0; i < safePlayers.size(); i++) {
            PlayerRequestDto dto = safePlayers.get(i);
            try {
                Set<Integer> allUsedNumbers = new HashSet<>();
                allUsedNumbers.addAll(existingNumbers);
                allUsedNumbers.addAll(usedNumbersInBatch);

                validatePlayerNumberUniqueness(dto.getNumber(), allUsedNumbers);
                validatePlayerData(dto);

                Player player = playerMapper.toEntity(dto);
                player.setTeam(team);
                player.setPosition(Position.valueOf(dto.getPosition().toUpperCase()));
                player.setGoals(dto.getGoals());
                player.setAssists(dto.getAssists());

                Player saved = playerRepository.save(player);
                usedNumbersInBatch.add(saved.getNumber());
                createdPlayers.add(playerMapper.toResponseDto(saved));
                log.debug("Successfully created player at index {}: {} {}", i, dto.getName(), dto.getSurname());

            } catch (Exception ex) {
                String errorMsg = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
                failures.put(i, errorMsg);
                log.warn("Failed to create player at index {}: {}", i, errorMsg);
            }
        }

        if (!createdPlayers.isEmpty()) {
            playerService.invalidateSearchCache();
        }

        log.info("Non-transactional bulk creation completed: {} succeeded, {} failed",
                createdPlayers.size(), failures.size());

        if (!failures.isEmpty()) {
            throw new PartialBulkCreationException(
                    "Some players could not be created",
                    createdPlayers.size(),
                    failures.size(),
                    failures,
                    createdPlayers
            );
        }

        return createdPlayers;
    }

    private void validatePlayerForTeam(PlayerRequestDto dto, Set<Integer> existingNumbers, List<Player> playersToSave) {
        validatePlayerNumberUniqueness(dto.getNumber(), existingNumbers);
        validatePlayerNumberDuplicateInBatch(dto.getNumber(), playersToSave);
        validatePlayerData(dto);
    }

    private void validatePlayerNumberUniqueness(Integer number, Set<Integer> existingNumbers) {
        if (existingNumbers.contains(number)) {
            throw new IllegalArgumentException(
                    String.format("Player number %d already exists in this team", number)
            );
        }
    }

    private void validatePlayerNumberDuplicateInBatch(Integer number, List<Player> playersToSave) {
        boolean duplicateInBatch = playersToSave.stream()
                .anyMatch(p -> p.getNumber().equals(number));

        if (duplicateInBatch) {
            throw new IllegalArgumentException(
                    String.format("Duplicate player number %d in the same bulk request", number)
            );
        }
    }

    private void validatePlayerData(PlayerRequestDto dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Player name is required");
        }
        if (dto.getSurname() == null || dto.getSurname().isBlank()) {
            throw new IllegalArgumentException("Player surname is required");
        }
        if (dto.getNumber() == null || dto.getNumber() < 1 || dto.getNumber() > 99) {
            throw new IllegalArgumentException("Player number must be between 1 and 99");
        }
        if (dto.getAge() == null || dto.getAge() < 16 || dto.getAge() > 50) {
            throw new IllegalArgumentException("Player age must be between 16 and 50");
        }
        try {
            Position.valueOf(dto.getPosition().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid position: " + dto.getPosition() +
                    ". Must be GOALKEEPER, DEFENDER, or FORWARD");
        }
        if (dto.getGoals() == null || dto.getGoals() < 0) {
            throw new IllegalArgumentException("Goals cannot be negative");
        }
        if (dto.getAssists() == null || dto.getAssists() < 0) {
            throw new IllegalArgumentException("Assists cannot be negative");
        }
    }
}
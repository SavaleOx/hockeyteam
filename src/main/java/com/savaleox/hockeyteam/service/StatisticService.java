package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.StatisticRequestDto;
import com.savaleox.hockeyteam.dto.StatisticResponseDto;
import com.savaleox.hockeyteam.mapper.StatisticMapper;
import com.savaleox.hockeyteam.model.entity.Player;
import com.savaleox.hockeyteam.model.entity.Statistic;
import com.savaleox.hockeyteam.repository.PlayerRepository;
import com.savaleox.hockeyteam.repository.StatisticRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticService {
    private final StatisticRepository statisticRepository;
    private final PlayerRepository playerRepository;
    private final StatisticMapper statisticMapper;
    private final PlayerService playerService;

    public StatisticService(StatisticRepository statisticRepository,
                            PlayerRepository playerRepository,
                            StatisticMapper statisticMapper, PlayerService playerService) {
        this.statisticRepository = statisticRepository;
        this.playerRepository = playerRepository;
        this.statisticMapper = statisticMapper;
        this.playerService = playerService;
    }

    public List<StatisticResponseDto> getByPlayer(Long playerId) {
        return statisticRepository.findByPlayerId(playerId).stream()
                .map(statisticMapper::toResponseDto)
                .toList();
    }

    public List<StatisticResponseDto> getByPlayerAndSeason(Long playerId, Integer season) {
        return statisticRepository.findByPlayerIdAndSeason(playerId, season).stream()
                .map(statisticMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public StatisticResponseDto create(StatisticRequestDto dto) {
        Player player = playerRepository.findById(dto.getPlayerId())
                .orElseThrow();

        Statistic statistic = statisticMapper.toEntity(dto);
        statistic.setPlayer(player);

        Statistic saved = statisticRepository.save(statistic);
        if (dto.getGoals() < 0 || dto.getAssists() < 0) {
            throw new IllegalArgumentException("Goals and assists cannot be negative");
        }
        player.setGoals(player.getGoals() + dto.getGoals());
        player.setAssists(player.getAssists() + dto.getAssists());
        playerRepository.save(player);
        playerService.invalidateSearchCache();
        return statisticMapper.toResponseDto(saved);
    }

    public StatisticResponseDto createWithoutTransactional(StatisticRequestDto dto) {
        Player player = playerRepository.findById(dto.getPlayerId())
                .orElseThrow();
        Statistic statistic = statisticMapper.toEntity(dto);
        statistic.setPlayer(player);
        Statistic saved = statisticRepository.save(statistic);
        if (dto.getGoals() < 0 && dto.getAssists() < 0) {
            throw new IllegalArgumentException("Goals and assists cannot be negative");
        }
        player.setGoals(player.getGoals() + dto.getGoals());
        player.setAssists(player.getAssists() + dto.getAssists());
        playerRepository.save(player);

        return statisticMapper.toResponseDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        Statistic statistic = statisticRepository.findById(id)
                .orElseThrow();

        Player player = statistic.getPlayer();
        player.setGoals(player.getGoals() - statistic.getGoals());
        player.setAssists(player.getAssists() - statistic.getAssists());
        playerRepository.save(player);
        playerService.invalidateSearchCache();
        statisticRepository.delete(statistic);
    }

    @Transactional
    public StatisticResponseDto update(Long id, StatisticRequestDto dto) {
        Statistic statistic = statisticRepository.findById(id)
                .orElseThrow();
        Player player = statistic.getPlayer();
        player.setGoals(player.getGoals() - statistic.getGoals());
        player.setAssists(player.getAssists() - statistic.getAssists());

        if (dto.getSeason() != null) statistic.setSeason(dto.getSeason());
        if (dto.getGoals() != null) statistic.setGoals(dto.getGoals());
        if (dto.getAssists() != null) statistic.setAssists(dto.getAssists());
        if (dto.getGames() != null) statistic.setGames(dto.getGames());

        if (dto.getPlayerId() != null && !dto.getPlayerId().equals(player.getId())) {
            Player newPlayer = playerRepository.findById(dto.getPlayerId())
                    .orElseThrow();
            statistic.setPlayer(newPlayer);
            player = newPlayer;
        }

        player.setGoals(player.getGoals() + statistic.getGoals());
        player.setAssists(player.getAssists() + statistic.getAssists());
        playerRepository.save(player);

        Statistic saved = statisticRepository.save(statistic);
        return statisticMapper.toResponseDto(saved);
    }

    @Transactional
    public StatisticResponseDto patch(Long id, StatisticRequestDto dto) {
        Statistic statistic = statisticRepository.findById(id)
                .orElseThrow();
        Player player = statistic.getPlayer();

        int oldGoals = statistic.getGoals();
        int oldAssists = statistic.getAssists();

        if (dto.getSeason() != null) statistic.setSeason(dto.getSeason());
        if (dto.getGames() != null) statistic.setGames(dto.getGames());
        if (dto.getGoals() != null) statistic.setGoals(dto.getGoals());
        if (dto.getAssists() != null) statistic.setAssists(dto.getAssists());

        int deltaGoals = statistic.getGoals() - oldGoals;
        int deltaAssists = statistic.getAssists() - oldAssists;
        player.setGoals(player.getGoals() + deltaGoals);
        player.setAssists(player.getAssists() + deltaAssists);

        if (dto.getPlayerId() != null && !dto.getPlayerId().equals(player.getId())) {
            player.setGoals(player.getGoals() - deltaGoals);
            player.setAssists(player.getAssists() - deltaAssists);
            playerRepository.save(player);


            Player newPlayer = playerRepository.findById(dto.getPlayerId())
                    .orElseThrow();
            statistic.setPlayer(newPlayer);
            player = newPlayer;
            player.setGoals(player.getGoals() + statistic.getGoals());
            player.setAssists(player.getAssists() + statistic.getAssists());
        } else {
            playerRepository.save(player);
        }

        Statistic saved = statisticRepository.save(statistic);
        playerService.invalidateSearchCache();
        return statisticMapper.toResponseDto(saved);
    }
}
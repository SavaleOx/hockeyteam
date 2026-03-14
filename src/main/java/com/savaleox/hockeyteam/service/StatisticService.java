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

    public StatisticService(StatisticRepository statisticRepository,
                            PlayerRepository playerRepository,
                            StatisticMapper statisticMapper) {
        this.statisticRepository = statisticRepository;
        this.playerRepository = playerRepository;
        this.statisticMapper = statisticMapper;
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
                .orElseThrow(() -> new RuntimeException("Player not found"));

        Statistic statistic = statisticMapper.toEntity(dto);
        statistic.setPlayer(player);

        Statistic saved = statisticRepository.save(statistic);

        player.setGoals(player.getGoals() + dto.getGoals());
        player.setAssists(player.getAssists() + dto.getAssists());
        playerRepository.save(player);

        return statisticMapper.toResponseDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        Statistic statistic = statisticRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Statistic not found"));

        Player player = statistic.getPlayer();
        player.setGoals(player.getGoals() - statistic.getGoals());
        player.setAssists(player.getAssists() - statistic.getAssists());
        playerRepository.save(player);

        statisticRepository.delete(statistic);
    }
}
package com.savaleOx.hockeyteam.service;

import com.savaleOx.hockeyteam.dto.StatisticRequestDto;
import com.savaleOx.hockeyteam.dto.StatisticResponseDto;
import com.savaleOx.hockeyteam.mapper.StatisticMapper;
import com.savaleOx.hockeyteam.model.entity.Player;
import com.savaleOx.hockeyteam.model.entity.Statistic;
import com.savaleOx.hockeyteam.repository.PlayerRepository;
import com.savaleOx.hockeyteam.repository.StatisticRepository;
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
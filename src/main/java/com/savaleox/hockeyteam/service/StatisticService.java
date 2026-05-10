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
                            StatisticMapper statisticMapper,
                            PlayerService playerService) {
        this.statisticRepository = statisticRepository;
        this.playerRepository = playerRepository;
        this.statisticMapper = statisticMapper;
        this.playerService = playerService;
    }

    // Получение статистики по игроку
    public List<StatisticResponseDto> getByPlayer(Long playerId) {
        return statisticRepository.findByPlayerId(playerId).stream()
                .map(statisticMapper::toResponseDto)
                .toList();
    }

    // Получение статистики по игроку и сезону (обычно одна запись)
    public List<StatisticResponseDto> getByPlayerAndSeason(Long playerId, Integer season) {
        return statisticRepository.findByPlayerIdAndSeason(playerId, season).stream()
                .map(statisticMapper::toResponseDto)
                .toList();
    }

    // Создание новой статистики
    @Transactional
    public StatisticResponseDto create(StatisticRequestDto dto) {
        Long playerId = dto.getPlayerId();
        Integer season = dto.getSeason();

        // Проверка: существует ли уже статистика для данного игрока и сезона
        if (statisticRepository.existsByPlayerIdAndSeason(playerId, season)) {
            throw new IllegalArgumentException(
                    String.format("Статистика для игрока %d за сезон %d уже существует", playerId, season)
            );
        }

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found with id: " + playerId));

        Statistic statistic = statisticMapper.toEntity(dto);
        statistic.setPlayer(player);
        statistic.setSeason(season);
        statistic.setGoals(dto.getGoals());
        statistic.setAssists(dto.getAssists());
        statistic.setGames(dto.getGames());

        Statistic saved = statisticRepository.save(statistic);

        // Обновляем суммарные показатели игрока (голы, ассисты)
        updatePlayerTotals(player, dto.getGoals(), dto.getAssists(), true);

        playerService.invalidateSearchCache();

        return statisticMapper.toResponseDto(saved);
    }

    // Удаление статистики
    @Transactional
    public void delete(Long id) {
        Statistic statistic = statisticRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Statistic not found with id: " + id));

        Player player = statistic.getPlayer();
        int goalsToSubtract = statistic.getGoals();
        int assistsToSubtract = statistic.getAssists();

        statisticRepository.delete(statistic);

        // Обновляем суммарные показатели игрока (уменьшаем)
        updatePlayerTotals(player, -goalsToSubtract, -assistsToSubtract, true);

        playerService.invalidateSearchCache();
    }

    // Полное обновление статистики (PUT)
    @Transactional
    public StatisticResponseDto update(Long id, StatisticRequestDto dto) {
        Statistic existing = statisticRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Statistic not found with id: " + id));

        Long oldPlayerId = existing.getPlayer().getId();
        Integer oldSeason = existing.getSeason();
        Long newPlayerId = dto.getPlayerId() != null ? dto.getPlayerId() : oldPlayerId;
        Integer newSeason = dto.getSeason() != null ? dto.getSeason() : oldSeason;

        // Если меняется пара (игрок, сезон) - проверяем, что новая комбинация свободна
        if (!oldPlayerId.equals(newPlayerId) || !oldSeason.equals(newSeason)) {
            if (statisticRepository.existsByPlayerIdAndSeason(newPlayerId, newSeason)) {
                throw new IllegalArgumentException(
                        String.format("Статистика для игрока %d за сезон %d уже существует", newPlayerId, newSeason)
                );
            }
        }

        Player player = existing.getPlayer();
        // Откатываем старые значения из суммарной статистики игрока
        updatePlayerTotals(player, -existing.getGoals(), -existing.getAssists(), false); // false - не сохранять пока

        // Обновляем поля статистики
        existing.setSeason(newSeason);
        existing.setGames(dto.getGames());
        existing.setGoals(dto.getGoals());
        existing.setAssists(dto.getAssists());

        // Если меняется игрок, меняем связь
        if (!oldPlayerId.equals(newPlayerId)) {
            Player newPlayer = playerRepository.findById(newPlayerId)
                    .orElseThrow(() -> new RuntimeException("New player not found with id: " + newPlayerId));
            existing.setPlayer(newPlayer);
            player = newPlayer;
        }

        // Добавляем новые значения к суммарной статистике игрока
        updatePlayerTotals(player, existing.getGoals(), existing.getAssists(), true);

        Statistic saved = statisticRepository.save(existing);
        playerService.invalidateSearchCache();

        return statisticMapper.toResponseDto(saved);
    }

    // Частичное обновление статистики (PATCH)
    @Transactional
    public StatisticResponseDto patch(Long id, StatisticRequestDto dto) {
        Statistic existing = statisticRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Statistic not found with id: " + id));

        Long oldPlayerId = existing.getPlayer().getId();
        Integer oldSeason = existing.getSeason();

        Long newPlayerId = dto.getPlayerId() != null ? dto.getPlayerId() : oldPlayerId;
        Integer newSeason = dto.getSeason() != null ? dto.getSeason() : oldSeason;

        // Проверка уникальности при смене пары (игрок, сезон)
        if (!oldPlayerId.equals(newPlayerId) || !oldSeason.equals(newSeason)) {
            if (statisticRepository.existsByPlayerIdAndSeason(newPlayerId, newSeason)) {
                throw new IllegalArgumentException(
                        String.format("Статистика для игрока %d за сезон %d уже существует", newPlayerId, newSeason)
                );
            }
        }

        Player player = existing.getPlayer();
        // Откатываем старые значения
        updatePlayerTotals(player, -existing.getGoals(), -existing.getAssists(), false);

        // Применяем только переданные поля
        if (dto.getSeason() != null) existing.setSeason(dto.getSeason());
        if (dto.getGames() != null) existing.setGames(dto.getGames());
        if (dto.getGoals() != null) existing.setGoals(dto.getGoals());
        if (dto.getAssists() != null) existing.setAssists(dto.getAssists());

        if (dto.getPlayerId() != null && !dto.getPlayerId().equals(oldPlayerId)) {
            Player newPlayer = playerRepository.findById(dto.getPlayerId())
                    .orElseThrow(() -> new RuntimeException("New player not found with id: " + dto.getPlayerId()));
            existing.setPlayer(newPlayer);
            player = newPlayer;
        }

        // Добавляем новые значения
        updatePlayerTotals(player, existing.getGoals(), existing.getAssists(), true);

        Statistic saved = statisticRepository.save(existing);
        playerService.invalidateSearchCache();

        return statisticMapper.toResponseDto(saved);
    }

    // Вспомогательный метод для обновления суммарных голов и ассистов игрока
    private void updatePlayerTotals(Player player, int deltaGoals, int deltaAssists, boolean flush) {
        if (player == null) return;
        player.setGoals(player.getGoals() + deltaGoals);
        player.setAssists(player.getAssists() + deltaAssists);
        if (flush) {
            playerRepository.save(player);
        }
    }
}
package com.savaleox.hockeyteam.repository;

import com.savaleox.hockeyteam.model.entity.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    List<Statistic> findByPlayerId(Long playerId);
    List<Statistic> findByPlayerIdAndSeason(Long playerId, Integer season);
}
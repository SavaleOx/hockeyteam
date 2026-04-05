package com.savaleox.hockeyteam.repository;

import com.savaleox.hockeyteam.model.entity.Statistic;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {

    @EntityGraph(attributePaths = {"player"})
    List<Statistic> findByPlayerId(Long playerId);

    @EntityGraph(attributePaths = {"player"})
    List<Statistic> findByPlayerIdAndSeason(Long playerId, Integer season);

    @EntityGraph(attributePaths = {"player"})
    Optional<Statistic> findById(Long id);
}
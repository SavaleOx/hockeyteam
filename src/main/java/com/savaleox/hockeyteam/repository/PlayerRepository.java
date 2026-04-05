package com.savaleox.hockeyteam.repository;

import com.savaleox.hockeyteam.model.entity.Player;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @EntityGraph(attributePaths = {"team", "position"})
    List<Player> findAll();

    @EntityGraph(attributePaths = {"team", "position"})
    Optional<Player> findById(Long id);

    @EntityGraph(attributePaths = {"team", "position"})
    List<Player> findByTeamId(Long teamId);

    @EntityGraph(attributePaths = {"team", "position"})
    List<Player> findByPositionName(String positionName);

    @EntityGraph(attributePaths = {"team", "position"})
    List<Player> findByTeamIdAndPositionName(Long teamId, String positionName);

    @EntityGraph(attributePaths = {"team", "position"})
    @Query("SELECT p FROM Player p WHERE p.goals >= :minGoals")
    List<Player> findByMinGoals(@Param("minGoals") Integer minGoals);
}
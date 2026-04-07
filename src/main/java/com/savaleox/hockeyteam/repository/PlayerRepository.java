package com.savaleox.hockeyteam.repository;

import com.savaleox.hockeyteam.model.entity.Player;
import com.savaleox.hockeyteam.model.enums.Position;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @EntityGraph(attributePaths = {"team"})
    List<Player> findAll();

    @EntityGraph(attributePaths = {"team"})
    Optional<Player> findById(Long id);

    @EntityGraph(attributePaths = {"team"})
    List<Player> findByTeamId(Long teamId);

    @EntityGraph(attributePaths = {"team"})
    List<Player> findByPosition(Position position);

    @EntityGraph(attributePaths = {"team"})
    List<Player> findByTeamIdAndPosition(Long teamId, Position position);

    @EntityGraph(attributePaths = {"team"})
    @Query("SELECT p FROM Player p WHERE p.goals >= :minGoals")
    List<Player> findByMinGoals(@Param("minGoals") Integer minGoals);
}
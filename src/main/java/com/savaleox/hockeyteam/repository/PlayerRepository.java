package com.savaleox.hockeyteam.repository;

import com.savaleox.hockeyteam.model.entity.Player;
import com.savaleox.hockeyteam.model.enums.Position;
import com.savaleox.hockeyteam.dto.PlayerSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @EntityGraph(attributePaths = {"team", "team.coach"})
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

    @Query("""
    SELECT p
    FROM Player p
    JOIN p.team t
    LEFT JOIN FETCH p.statistics s
    WHERE p.id = COALESCE(:#{#criteria.playerId}, p.id)
      AND p.age = COALESCE(:#{#criteria.playerAge}, p.age)
      AND p.number = COALESCE(:#{#criteria.playerNumber}, p.number)
      AND t.name = COALESCE(:#{#criteria.teamName}, t.name)
      AND p.position = COALESCE(:#{#criteria.playerPosition}, p.position)
      AND p.goals >= COALESCE(:#{#criteria.minGoals}, p.goals)
      AND p.goals <= COALESCE(:#{#criteria.maxGoals}, p.goals)
      AND p.assists >= COALESCE(:#{#criteria.minAssists}, p.assists)
      AND p.assists <= COALESCE(:#{#criteria.maxAssists}, p.assists)
    """)
    Page<Player> searchWithFiltersJPQL(
            @Param("criteria") PlayerSearchCriteria criteria,
            Pageable pageable
    );

    @Query(value = """
        SELECT p.*
        FROM players p
        LEFT JOIN teams t ON p.team_id = t.id
        WHERE p.id = COALESCE(CAST(:#{#criteria.playerId} AS bigint), p.id)
          AND p.age = COALESCE(CAST(:#{#criteria.playerAge} AS int), p.age)
          AND p.number = COALESCE(CAST(:#{#criteria.playerNumber} AS int), p.number)
          AND t.name = COALESCE(:#{#criteria.teamName}, t.name)
          AND p.position = COALESCE(:#{#criteria.playerPosition}, p.position)
          AND p.goals >= COALESCE(CAST(:#{#criteria.minGoals} AS int), p.goals)
          AND p.goals <= COALESCE(CAST(:#{#criteria.maxGoals} AS int), p.goals)
          AND p.assists >= COALESCE(CAST(:#{#criteria.minAssists} AS int), p.assists)
          AND p.assists <= COALESCE(CAST(:#{#criteria.maxAssists} AS int), p.assists)
        """,
            countQuery = """
        SELECT COUNT(*)
        FROM players p
        LEFT JOIN teams t ON p.team_id = t.id
        WHERE p.id = COALESCE(CAST(:#{#criteria.playerId} AS bigint), p.id)
          AND p.age = COALESCE(CAST(:#{#criteria.playerAge} AS int), p.age)
          AND p.number = COALESCE(CAST(:#{#criteria.playerNumber} AS int), p.number)
          AND t.name = COALESCE(:#{#criteria.teamName}, t.name)
          AND p.position = COALESCE(:#{#criteria.playerPosition}, p.position)
          AND p.goals >= COALESCE(CAST(:#{#criteria.minGoals} AS int), p.goals)
          AND p.goals <= COALESCE(CAST(:#{#criteria.maxGoals} AS int), p.goals)
          AND p.assists >= COALESCE(CAST(:#{#criteria.minAssists} AS int), p.assists)
          AND p.assists <= COALESCE(CAST(:#{#criteria.maxAssists} AS int), p.assists)
        """,
            nativeQuery = true)
    Page<Player> searchWithFiltersNative(@Param("criteria") PlayerSearchCriteria criteria, Pageable pageable);
}

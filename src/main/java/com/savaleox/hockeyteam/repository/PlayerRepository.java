package com.savaleox.hockeyteam.repository;

import com.savaleox.hockeyteam.model.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByTeamId(Long teamId);
    List<Player> findByPositionName(String positionName);
    List<Player> findByTeamIdAndPositionName(Long teamId, String positionName);

    @Query("SELECT p FROM Player p WHERE p.goals >= :minGoals")
    List<Player> findByMinGoals(@Param("minGoals") Integer minGoals);
}
package com.savaleox.hockeyteam.repository;

import com.savaleox.hockeyteam.model.entity.Team;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Team findByName(String name);

    @EntityGraph(attributePaths = {"players", "coach"})
    List<Team> findAll();

    @EntityGraph(attributePaths = {"players", "coach"})
    Optional<Team> findById(Long id);
}
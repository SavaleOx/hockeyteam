package com.savaleox.hockeyteam.repository;

import com.savaleox.hockeyteam.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByName(String name);
}
package com.savaleOx.hockeyteam.repository;

import com.savaleOx.hockeyteam.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByName(String name);
}
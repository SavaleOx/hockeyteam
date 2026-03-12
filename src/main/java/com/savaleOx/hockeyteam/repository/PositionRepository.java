package com.savaleOx.hockeyteam.repository;

import com.savaleOx.hockeyteam.model.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Position findByName(String name);
}
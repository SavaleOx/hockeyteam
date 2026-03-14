package com.savaleox.hockeyteam.repository;

import com.savaleox.hockeyteam.model.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Position findByName(String name);
}
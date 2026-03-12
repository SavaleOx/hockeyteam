package com.savaleOx.hockeyteam.repository;

import com.savaleOx.hockeyteam.model.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Achievement findByName(String name);
}
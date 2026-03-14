package com.savaleox.hockeyteam.repository;

import com.savaleox.hockeyteam.model.entity.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    Achievement findByName(String name);
}
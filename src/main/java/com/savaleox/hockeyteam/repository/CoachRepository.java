package com.savaleox.hockeyteam.repository;

import com.savaleox.hockeyteam.model.entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CoachRepository extends JpaRepository<Coach, Long> {
    List<Coach> findByTeamId(Long teamId);
}
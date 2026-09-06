package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.Achievement;

public interface AchievementRepo extends JpaRepository<Achievement, Long> {
	List<Achievement> findByPlayerId(Long playerId);
}

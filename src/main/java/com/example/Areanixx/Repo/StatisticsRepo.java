package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.Statistics;

public interface StatisticsRepo extends JpaRepository<Statistics, Long> {
	List<Statistics> findByPlayerId(Long playerId);
}

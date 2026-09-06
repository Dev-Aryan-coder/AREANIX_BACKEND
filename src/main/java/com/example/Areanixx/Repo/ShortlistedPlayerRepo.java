package com.example.Areanixx.Repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.ShortlistedPlayer;

public interface ShortlistedPlayerRepo extends JpaRepository<ShortlistedPlayer, Long> {
	List<ShortlistedPlayer> findByRecruiterId(Long recruiterId);
	List<ShortlistedPlayer> findByPlayerId(Long playerId);
	long countByPlayerId(Long playerId);
	Optional<ShortlistedPlayer> findByRecruiterIdAndPlayerId(Long recruiterId, Long playerId);
}

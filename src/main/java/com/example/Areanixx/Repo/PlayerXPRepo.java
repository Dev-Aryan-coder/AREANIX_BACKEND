package com.example.Areanixx.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.PlayerXP;

public interface PlayerXPRepo extends JpaRepository<PlayerXP, Long> {
	PlayerXP findByPlayerId(Long playerId);
}

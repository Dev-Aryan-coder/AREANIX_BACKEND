package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.TournamentResult;

public interface TournamentResultRepo extends JpaRepository<TournamentResult, Long> {
	List<TournamentResult> findByTournamentIdOrderByPlacementAsc(Long tournamentId);
}

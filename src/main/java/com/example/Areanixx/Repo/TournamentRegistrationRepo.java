package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.RegistrationStatus;
import com.example.Areanixx.Entity.TournamentRegistration;

public interface TournamentRegistrationRepo extends JpaRepository<TournamentRegistration, Long> {
	List<TournamentRegistration> findByTournamentIdAndStatus(Long tournamentId, RegistrationStatus status);
	List<TournamentRegistration> findByPlayerId(Long playerId);
	List<TournamentRegistration> findByTeamId(Long teamId);
	List<TournamentRegistration> findByTournamentIdAndTeamId(Long tournamentId, Long teamId);
	List<TournamentRegistration> findByTournamentIdAndPlayerId(Long tournamentId, Long playerId);
}

package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.Tournament;
import com.example.Areanixx.Entity.TournamentStatus;

public interface TournamentRepo extends JpaRepository<Tournament, Long> {
	List<Tournament> findByStatus(TournamentStatus status);
	List<Tournament> findByOrganizerId(Long organizerId);
}

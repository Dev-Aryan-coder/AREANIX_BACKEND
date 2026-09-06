package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.Dispute;
import com.example.Areanixx.Entity.ReportStatus;

public interface DisputeRepo extends JpaRepository<Dispute, Long> {
	List<Dispute> findByStatus(ReportStatus status);
	List<Dispute> findByTournamentIdIn(List<Long> tournamentIds);
	List<Dispute> findByTournamentIdInAndStatus(List<Long> tournamentIds, ReportStatus status);
}

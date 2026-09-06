package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.ReportStatus;
import com.example.Areanixx.Entity.TournamentReport;

public interface TournamentReportRepo extends JpaRepository<TournamentReport, Long> {
	List<TournamentReport> findByStatus(ReportStatus status);
}

package com.example.Areanixx.Entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;

@Entity
public class TournamentReport {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getTournamentId() { return tournamentId; }
	public void setTournamentId(Long tournamentId) { this.tournamentId = tournamentId; }
	public Long getReportedBy() { return reportedBy; }
	public void setReportedBy(Long reportedBy) { this.reportedBy = reportedBy; }
	public String getReason() { return reason; }
	public void setReason(String reason) { this.reason = reason; }
	public ReportStatus getStatus() { return status; }
	public void setStatus(ReportStatus status) { this.status = status; }
	public Instant getCreatedAt() { return createdAt; }
	public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Tournament id is required....")
	private Long tournamentId;

	@NotNull(message = "Reported by user id is required....")
	private Long reportedBy;

	@Lob
	private String reason;

	@Enumerated(EnumType.STRING)
	private ReportStatus status = ReportStatus.OPEN;

	private Instant createdAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = Instant.now();
	}
}

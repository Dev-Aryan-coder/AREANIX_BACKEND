package com.example.Areanixx.Entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;

@Entity
public class Invite {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getRecruiterId() { return recruiterId; }
	public void setRecruiterId(Long recruiterId) { this.recruiterId = recruiterId; }
	public Long getPlayerId() { return playerId; }
	public void setPlayerId(Long playerId) { this.playerId = playerId; }
	public InviteStatus getStatus() { return status; }
	public void setStatus(InviteStatus status) { this.status = status; }
	public Instant getCreatedAt() { return createdAt; }
	public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Recruiter id is required....")
	private Long recruiterId;

	@NotNull(message = "Player id is required....")
	private Long playerId;

	@Enumerated(EnumType.STRING)
	private InviteStatus status = InviteStatus.PENDING;

	private Instant createdAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = Instant.now();
	}
}

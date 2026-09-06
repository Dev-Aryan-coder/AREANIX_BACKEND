package com.example.Areanixx.Entity;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;

@Entity
public class TeamMember {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getTeamId() { return teamId; }
	public void setTeamId(Long teamId) { this.teamId = teamId; }
	public Team getTeam() { return team; }
	public void setTeam(Team team) { this.team = team; }
	public Long getPlayerId() { return playerId; }
	public void setPlayerId(Long playerId) { this.playerId = playerId; }
	public PlayerProfile getPlayer() { return player; }
	public void setPlayer(PlayerProfile player) { this.player = player; }
	public Instant getJoinedAt() { return joinedAt; }
	public void setJoinedAt(Instant joinedAt) { this.joinedAt = joinedAt; }
	public Instant getLeftAt() { return leftAt; }
	public void setLeftAt(Instant leftAt) { this.leftAt = leftAt; }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Team id is required....")
	@Column(name = "team_id")
	private Long teamId;

	@ManyToOne
	@JoinColumn(name = "team_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Team team;

	@NotNull(message = "Player id is required....")
	@Column(name = "player_id")
	private Long playerId;

	@ManyToOne
	@JoinColumn(name = "player_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private PlayerProfile player;

	private Instant joinedAt;

	// null while still on the team
	private Instant leftAt;

	@PrePersist
	protected void onCreate() {
		this.joinedAt = Instant.now();
	}

}

package com.example.Areanixx.Entity;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;

@Entity
public class Statistics {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getPlayerId() { return playerId; }
	public void setPlayerId(Long playerId) { this.playerId = playerId; }
	public PlayerProfile getPlayer() { return player; }
	public void setPlayer(PlayerProfile player) { this.player = player; }
	public String getGame() { return game; }
	public void setGame(String game) { this.game = game; }
	public String getStatSnapshot() { return statSnapshot; }
	public void setStatSnapshot(String statSnapshot) { this.statSnapshot = statSnapshot; }
	public Instant getRecordedAt() { return recordedAt; }
	public void setRecordedAt(Instant recordedAt) { this.recordedAt = recordedAt; }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Player id is required....")
	@Column(name = "player_id")
	private Long playerId;

	@ManyToOne
	@JoinColumn(name = "player_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private PlayerProfile player;

	@NotNull(message = "Game is required....")
	private String game;

	// stored as JSON text, e.g. {"kills":10,"wins":2,"kd":3.5}
	@Lob
	private String statSnapshot;

	private Instant recordedAt;

	@PrePersist
	protected void onCreate() {
		this.recordedAt = Instant.now();
	}

}

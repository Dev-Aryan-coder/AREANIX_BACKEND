package com.example.Areanixx.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class PlayerXP {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getPlayerId() { return playerId; }
	public void setPlayerId(Long playerId) { this.playerId = playerId; }
	public PlayerProfile getPlayer() { return player; }
	public void setPlayer(PlayerProfile player) { this.player = player; }
	public long getTotalXp() { return totalXp; }
	public void setTotalXp(long totalXp) { this.totalXp = totalXp; }
	public int getCurrentLevel() { return currentLevel; }
	public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Player id is required....")
	@Column(name = "player_id", unique = true)
	private Long playerId;

	@OneToOne
	@JoinColumn(name = "player_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private PlayerProfile player;

	// this is a cached total - always recomputable by summing XPTransaction rows
	private long totalXp = 0;

	private int currentLevel = 1;

}

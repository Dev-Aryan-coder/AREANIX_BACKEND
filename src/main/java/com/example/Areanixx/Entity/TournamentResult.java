package com.example.Areanixx.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class TournamentResult {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getTournamentId() { return tournamentId; }
	public void setTournamentId(Long tournamentId) { this.tournamentId = tournamentId; }
	public Tournament getTournament() { return tournament; }
	public void setTournament(Tournament tournament) { this.tournament = tournament; }
	public Long getPlayerId() { return playerId; }
	public void setPlayerId(Long playerId) { this.playerId = playerId; }
	public PlayerProfile getPlayer() { return player; }
	public void setPlayer(PlayerProfile player) { this.player = player; }
	public Long getTeamId() { return teamId; }
	public void setTeamId(Long teamId) { this.teamId = teamId; }
	public Team getTeam() { return team; }
	public void setTeam(Team team) { this.team = team; }
	public int getPlacement() { return placement; }
	public void setPlacement(int placement) { this.placement = placement; }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Tournament id is required....")
	@Column(name = "tournament_id")
	private Long tournamentId;

	@ManyToOne
	@JoinColumn(name = "tournament_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Tournament tournament;

	// either player (solo) or team (squad) will be set
	@Column(name = "player_id")
	private Long playerId;

	@ManyToOne
	@JoinColumn(name = "player_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private PlayerProfile player;

	@Column(name = "team_id")
	private Long teamId;

	@ManyToOne
	@JoinColumn(name = "team_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Team team;

	@Positive(message = "placement must be greater than zero...")
	private int placement;

}

package com.example.Areanixx.Entity;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

// "match" is a reserved keyword in MySQL, so the table is named "matches" instead
@Table(name = "matches")
@Entity
public class Match {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getTournamentId() { return tournamentId; }
	public void setTournamentId(Long tournamentId) { this.tournamentId = tournamentId; }
	public Tournament getTournament() { return tournament; }
	public void setTournament(Tournament tournament) { this.tournament = tournament; }
	public int getRound() { return round; }
	public void setRound(int round) { this.round = round; }
	public Long getTeamAId() { return teamAId; }
	public void setTeamAId(Long teamAId) { this.teamAId = teamAId; }
	public Team getTeamA() { return teamA; }
	public void setTeamA(Team teamA) { this.teamA = teamA; }
	public Long getTeamBId() { return teamBId; }
	public void setTeamBId(Long teamBId) { this.teamBId = teamBId; }
	public Team getTeamB() { return teamB; }
	public void setTeamB(Team teamB) { this.teamB = teamB; }
	public Instant getScheduledTime() { return scheduledTime; }
	public void setScheduledTime(Instant scheduledTime) { this.scheduledTime = scheduledTime; }
	public String getResult() { return result; }
	public void setResult(String result) { this.result = result; }

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

	private int round;

	@Column(name = "team_a_id")
	private Long teamAId;

	@ManyToOne
	@JoinColumn(name = "team_a_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Team teamA;

	@Column(name = "team_b_id")
	private Long teamBId;

	@ManyToOne
	@JoinColumn(name = "team_b_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Team teamB;

	private Instant scheduledTime;

	// null until match is actually played
	private String result;

}

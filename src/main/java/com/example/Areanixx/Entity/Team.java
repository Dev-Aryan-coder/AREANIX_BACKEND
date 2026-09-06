package com.example.Areanixx.Entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Team {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getRegion() { return region; }
	public void setRegion(String region) { this.region = region; }
	public String getGameFocus() { return gameFocus; }
	public void setGameFocus(String gameFocus) { this.gameFocus = gameFocus; }
	public Long getManagerId() { return managerId; }
	public void setManagerId(Long managerId) { this.managerId = managerId; }
	public Long getCoManagerId() { return coManagerId; }
	public void setCoManagerId(Long coManagerId) { this.coManagerId = coManagerId; }
	public User getManager() { return manager; }
	public void setManager(User manager) { this.manager = manager; }
	public String getLogo() { return logo; }
	public void setLogo(String logo) { this.logo = logo; }

	public List<TeamMember> getMembers() { return members; }
	public void setMembers(List<TeamMember> members) { this.members = members; }
	public List<Match> getMatchesAsTeamA() { return matchesAsTeamA; }
	public void setMatchesAsTeamA(List<Match> matchesAsTeamA) { this.matchesAsTeamA = matchesAsTeamA; }
	public List<Match> getMatchesAsTeamB() { return matchesAsTeamB; }
	public void setMatchesAsTeamB(List<Match> matchesAsTeamB) { this.matchesAsTeamB = matchesAsTeamB; }
	public List<TournamentRegistration> getRegistrations() { return registrations; }
	public void setRegistrations(List<TournamentRegistration> registrations) { this.registrations = registrations; }
	public List<TournamentResult> getResults() { return results; }
	public void setResults(List<TournamentResult> results) { this.results = results; }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Team name is required....")
	@Size(min = 3, max = 25, message = "give team name between 3 to 25 characters..Pls")
	private String name;

	@NotNull(message = "Region is required....")
	private String region;

	private String gameFocus;

	@NotNull(message = "Manager id is required....")
	@Column(name = "manager_id")
	private Long managerId;

	@Column(name = "co_manager_id")
	private Long coManagerId;

	@ManyToOne
	@JoinColumn(name = "manager_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User manager;

	@Lob
	private String logo;

	@JsonIgnore
	@OneToMany(mappedBy = "team")
	private List<TeamMember> members;

	@JsonIgnore
	@OneToMany(mappedBy = "teamA")
	private List<Match> matchesAsTeamA;

	@JsonIgnore
	@OneToMany(mappedBy = "teamB")
	private List<Match> matchesAsTeamB;

	@JsonIgnore
	@OneToMany(mappedBy = "team")
	private List<TournamentRegistration> registrations;

	@JsonIgnore
	@OneToMany(mappedBy = "team")
	private List<TournamentResult> results;

}

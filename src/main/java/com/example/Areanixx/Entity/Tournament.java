package com.example.Areanixx.Entity;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Tournament {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getOrganizerId() { return organizerId; }
	public void setOrganizerId(Long organizerId) { this.organizerId = organizerId; }
	public Organizer getOrganizer() { return organizer; }
	public void setOrganizer(Organizer organizer) { this.organizer = organizer; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getGame() { return game; }
	public void setGame(String game) { this.game = game; }
	public String getFormat() { return format; }
	public void setFormat(String format) { this.format = format; }
	public String getRegion() { return region; }
	public void setRegion(String region) { this.region = region; }
	public double getPrizePool() { return prizePool; }
	public void setPrizePool(double prizePool) { this.prizePool = prizePool; }
	public Boolean getPrizePoolPaid() { return prizePoolPaid; }
	public void setPrizePoolPaid(Boolean prizePoolPaid) { this.prizePoolPaid = prizePoolPaid; }
	public Instant getRegistrationOpenAt() { return registrationOpenAt; }
	public void setRegistrationOpenAt(Instant registrationOpenAt) { this.registrationOpenAt = registrationOpenAt; }
	public Instant getRegistrationCloseAt() { return registrationCloseAt; }
	public void setRegistrationCloseAt(Instant registrationCloseAt) { this.registrationCloseAt = registrationCloseAt; }
	public String getStreamLink() { return streamLink; }
	public void setStreamLink(String streamLink) { this.streamLink = streamLink; }
	public String getRules() { return rules; }
	public void setRules(String rules) { this.rules = rules; }
	public String getRoomId() { return roomId; }
	public void setRoomId(String roomId) { this.roomId = roomId; }
	public String getRoomPassword() { return roomPassword; }
	public void setRoomPassword(String roomPassword) { this.roomPassword = roomPassword; }
	public TournamentStatus getStatus() { return status; }
	public void setStatus(TournamentStatus status) { this.status = status; }

	public List<Match> getMatches() { return matches; }
	public void setMatches(List<Match> matches) { this.matches = matches; }
	public List<TournamentRegistration> getRegistrations() { return registrations; }
	public void setRegistrations(List<TournamentRegistration> registrations) { this.registrations = registrations; }
	public List<TournamentResult> getResults() { return results; }
	public void setResults(List<TournamentResult> results) { this.results = results; }
	public List<Achievement> getAchievements() { return achievements; }
	public void setAchievements(List<Achievement> achievements) { this.achievements = achievements; }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Organizer id is required....")
	@Column(name = "organizer_id")
	private Long organizerId;

	@ManyToOne
	@JoinColumn(name = "organizer_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Organizer organizer;

	@NotNull(message = "Tournament name is required....")
	@Size(min = 3, max = 50, message = "give tournament name between 3 to 50 characters..Pls")
	private String name;

	@NotNull(message = "Game is required....")
	private String game;

	private String format;

	@NotNull(message = "Region is required....")
	private String region;

	private double prizePool = 0;

	private Boolean prizePoolPaid = false;

	private Instant registrationOpenAt;
	private Instant registrationCloseAt;

	private String streamLink;

	@Lob
	private String rules;

	private String roomId;
	private String roomPassword;

	@Enumerated(EnumType.STRING)
	private TournamentStatus status = TournamentStatus.UPCOMING;

	@JsonIgnore
	@OneToMany(mappedBy = "tournament")
	private List<Match> matches;

	@JsonIgnore
	@OneToMany(mappedBy = "tournament")
	private List<TournamentRegistration> registrations;

	@JsonIgnore
	@OneToMany(mappedBy = "tournament")
	private List<TournamentResult> results;

	@JsonIgnore
	@OneToMany(mappedBy = "tournament")
	private List<Achievement> achievements;

}

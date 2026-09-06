package com.example.Areanixx.Entity;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
public class PlayerProfile {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getUserId() { return userId; }
	public void setUserId(Long userId) { this.userId = userId; }
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	public String getGamerTag() { return gamerTag; }
	public void setGamerTag(String gamerTag) { this.gamerTag = gamerTag; }
	public String getGame() { return game; }
	public void setGame(String game) { this.game = game; }
	public String getRankName() { return rankName; }
	public void setRankName(String rankName) { this.rankName = rankName; }
	public String getRoleInGame() { return roleInGame; }
	public void setRoleInGame(String roleInGame) { this.roleInGame = roleInGame; }
	public String getRegion() { return region; }
	public void setRegion(String region) { this.region = region; }
	public int getAge() { return age; }
	public void setAge(int age) { this.age = age; }
	public String getTwitchUrl() { return twitchUrl; }
	public void setTwitchUrl(String twitchUrl) { this.twitchUrl = twitchUrl; }
	public String getYoutubeUrl() { return youtubeUrl; }
	public void setYoutubeUrl(String youtubeUrl) { this.youtubeUrl = youtubeUrl; }
	public AvailabilityStatus getAvailabilityStatus() { return availabilityStatus; }
	public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) { this.availabilityStatus = availabilityStatus; }

	public PlayerXP getXp() { return xp; }
	public void setXp(PlayerXP xp) { this.xp = xp; }
	public List<Statistics> getStatistics() { return statistics; }
	public void setStatistics(List<Statistics> statistics) { this.statistics = statistics; }
	public List<TeamMember> getTeamMemberships() { return teamMemberships; }
	public void setTeamMemberships(List<TeamMember> teamMemberships) { this.teamMemberships = teamMemberships; }
	public List<XPTransaction> getXpTransactions() { return xpTransactions; }
	public void setXpTransactions(List<XPTransaction> xpTransactions) { this.xpTransactions = xpTransactions; }
	public List<Achievement> getAchievements() { return achievements; }
	public void setAchievements(List<Achievement> achievements) { this.achievements = achievements; }
	public List<TournamentRegistration> getRegistrations() { return registrations; }
	public void setRegistrations(List<TournamentRegistration> registrations) { this.registrations = registrations; }
	public List<TournamentResult> getResults() { return results; }
	public void setResults(List<TournamentResult> results) { this.results = results; }
	
	

	public String getProfileImageUrl() {
		return profileImageUrl;
	}
	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}



	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "User id is required....")
	@Column(name = "user_id", unique = true)
	private Long userId;

	@OneToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User user;

	@NotNull(message = "Gamer tag is required....")
	@Size(min = 3, max = 25, message = "give gamer tag between 3 to 25 characters..Pls")
	private String gamerTag;

	@NotNull(message = "Game is required....")
	private String game;

	private String rankName;
	private String roleInGame;

	@NotNull(message = "Region is required....")
	private String region;

	@Positive(message = "age must be greater than zero...")
	private int age;

	@Column(name = "profile_image_url")
	private String profileImageUrl;

	
	private String twitchUrl;
	private String youtubeUrl;

	@Enumerated(EnumType.STRING)
	private AvailabilityStatus availabilityStatus = AvailabilityStatus.NOT_AVAILABLE;

	@JsonIgnore
	@OneToOne(mappedBy = "player")
	private PlayerXP xp;

	@JsonIgnore
	@OneToMany(mappedBy = "player")
	private List<Statistics> statistics;

	@JsonIgnore
	@OneToMany(mappedBy = "player")
	private List<TeamMember> teamMemberships;

	@JsonIgnore
	@OneToMany(mappedBy = "player")
	private List<XPTransaction> xpTransactions;

	@JsonIgnore
	@OneToMany(mappedBy = "player")
	private List<Achievement> achievements;

	@JsonIgnore
	@OneToMany(mappedBy = "player")
	private List<TournamentRegistration> registrations;

	@JsonIgnore
	@OneToMany(mappedBy = "player")
	private List<TournamentResult> results;

}

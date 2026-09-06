package com.example.Areanixx.dto;

import java.util.List;

public class PlayerSearchResultDto {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getUserId() { return userId; }
	public void setUserId(Long userId) { this.userId = userId; }
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
	public Integer getAge() { return age; }
	public void setAge(Integer age) { this.age = age; }
	public String getTwitchUrl() { return twitchUrl; }
	public void setTwitchUrl(String twitchUrl) { this.twitchUrl = twitchUrl; }
	public String getYoutubeUrl() { return youtubeUrl; }
	public void setYoutubeUrl(String youtubeUrl) { this.youtubeUrl = youtubeUrl; }
	public String getProfileImageUrl() { return profileImageUrl; }
	public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
	public String getAvailabilityStatus() { return availabilityStatus; }
	public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }
	public long getTotalXp() { return totalXp; }
	public void setTotalXp(long totalXp) { this.totalXp = totalXp; }
	public int getCurrentLevel() { return currentLevel; }
	public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }
	public List<String> getAchievements() { return achievements; }
	public void setAchievements(List<String> achievements) { this.achievements = achievements; }
	public long getShortlistCount() { return shortlistCount; }
	public void setShortlistCount(long shortlistCount) { this.shortlistCount = shortlistCount; }

	private Long id;
	private Long userId;
	private String gamerTag;
	private String game;
	private String rankName;
	private String roleInGame;
	private String region;
	private Integer age;
	private String twitchUrl;
	private String youtubeUrl;
	private String profileImageUrl;
	private String availabilityStatus;
	private long totalXp;
	private int currentLevel;
	private List<String> achievements;
	private long shortlistCount;

	public PlayerSearchResultDto() {
	}
}

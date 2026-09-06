package com.example.Areanixx.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Areanixx.Entity.AvailabilityStatus;
import com.example.Areanixx.Entity.PlayerProfile;
import com.example.Areanixx.Entity.Statistics;
import com.example.Areanixx.Repo.AchievementRepo;
import com.example.Areanixx.Repo.PlayerProfileRepo;
import com.example.Areanixx.Repo.StatisticsRepo;
import com.example.Areanixx.Repo.TournamentRegistrationRepo;
import com.example.Areanixx.Repo.TournamentResultRepo;

@Service
public class PlayerService {

	@Autowired
	private PlayerProfileRepo playerRepo;
	@Autowired
	private AchievementRepo achievementRepo;
	@Autowired
	private StatisticsRepo statisticsRepo;
	@Autowired
	private TournamentResultRepo resultRepo;
	@Autowired
	private TournamentRegistrationRepo registrationRepo;

	public void createProfile(PlayerProfile p) {
		PlayerProfile existing = null;
		if (p.getUserId() != null) {
			existing = playerRepo.findByUserId(p.getUserId());
		}
		if (existing != null) {
			if (p.getGamerTag() != null) existing.setGamerTag(p.getGamerTag());
			if (p.getGame() != null) existing.setGame(p.getGame());
			if (p.getRankName() != null) existing.setRankName(p.getRankName());
			if (p.getRoleInGame() != null) existing.setRoleInGame(p.getRoleInGame());
			if (p.getRegion() != null) existing.setRegion(p.getRegion());
			if (p.getAge() > 0) existing.setAge(p.getAge());
			if (p.getProfileImageUrl() != null) existing.setProfileImageUrl(p.getProfileImageUrl());
			if (p.getTwitchUrl() != null) existing.setTwitchUrl(p.getTwitchUrl());
			if (p.getYoutubeUrl() != null) existing.setYoutubeUrl(p.getYoutubeUrl());
			if (p.getAvailabilityStatus() != null) existing.setAvailabilityStatus(p.getAvailabilityStatus());
			playerRepo.save(existing);
		} else {
			playerRepo.save(p);
		}
	}

	public PlayerProfile getbyid(Long id) {
		PlayerProfile p = playerRepo.findByUserId(id);
		if (p != null) return p;
		return playerRepo.findById(id).orElse(null);
	}

	public List<PlayerProfile> getAllPlayers() {
		return playerRepo.findAll();
	}

	public PlayerProfile patchprofile(long id, PlayerProfile p) {
		PlayerProfile existing = playerRepo.findById(id).orElse(null);
		if (existing == null) {
			existing = playerRepo.findByUserId(id);
		}
		if (existing == null) return null;
		
		if (p.getGamerTag() != null) existing.setGamerTag(p.getGamerTag());
		if (p.getGame() != null) existing.setGame(p.getGame());
		if (p.getRankName() != null) existing.setRankName(p.getRankName());
		if (p.getRoleInGame() != null) existing.setRoleInGame(p.getRoleInGame());
		if (p.getRegion() != null) existing.setRegion(p.getRegion());
		if (p.getAge() > 0) existing.setAge(p.getAge());
		if (p.getTwitchUrl() != null) existing.setTwitchUrl(p.getTwitchUrl());
		if (p.getYoutubeUrl() != null) existing.setYoutubeUrl(p.getYoutubeUrl());
		if (p.getProfileImageUrl() != null) existing.setProfileImageUrl(p.getProfileImageUrl());
		if (p.getAvailabilityStatus() != null) existing.setAvailabilityStatus(p.getAvailabilityStatus());
		
		return playerRepo.save(existing);
	}

	public PlayerProfile updateProfileImage(Long id, String imageUrl) {
		PlayerProfile p = getbyid(id);
		if (p == null) return null;
		p.setProfileImageUrl(imageUrl);
		return playerRepo.save(p);
	}

	public PlayerProfile updateAvailability(Long id, String status) {
		PlayerProfile p = getbyid(id);
		if (p == null) return null;
		p.setAvailabilityStatus(AvailabilityStatus.valueOf(status));
		return playerRepo.save(p);
	}

	public List<?> getTournamentHistory(Long id) {
		return resultRepo.findAll().stream()
				.filter(r -> r.getPlayerId() != null && r.getPlayerId().equals(id))
				.toList();
	}

	public List<?> getAchievements(Long id) {
		return achievementRepo.findByPlayerId(id);
	}

	public List<?> getStatistics(Long id) {
		return statisticsRepo.findByPlayerId(id);
	}

	public List<?> getRegistrations(Long id) {
		return registrationRepo.findByPlayerId(id);
	}

	public Statistics recordStatistics(Long playerId, String game, String statSnapshotJson) {
		Statistics s = new Statistics();
		s.setPlayerId(playerId);
		s.setGame(game);
		s.setStatSnapshot(statSnapshotJson);
		return statisticsRepo.save(s);
	}

	public List<PlayerProfile> getLiveStreamers(String game) {
		List<PlayerProfile> all = (game != null) ? playerRepo.findByGame(game) : playerRepo.findAll();
		return all.stream()
				.filter(p -> p.getTwitchUrl() != null || p.getYoutubeUrl() != null)
				.toList();
	}
}

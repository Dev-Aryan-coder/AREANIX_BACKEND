package com.example.Areanixx.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Areanixx.Entity.PlayerProfile;
import com.example.Areanixx.Entity.PlayerXP;
import com.example.Areanixx.Repo.PlayerProfileRepo;
import com.example.Areanixx.Repo.PlayerXPRepo;

@Service
public class LeaderboardService {

	@Autowired
	private PlayerProfileRepo playerProfileRepo;

	@Autowired
	private PlayerXPRepo playerXPRepo;

	public List<Object> getEnrichedXpLeaderboard(String game, String mode, long topN) {
		List<PlayerProfile> profiles = playerProfileRepo.findAll();

		List<Map<String, Object>> rows = new ArrayList<>();
		for (PlayerProfile profile : profiles) {
			if (game != null && !game.equalsIgnoreCase("all") && profile.getGame() != null
					&& !profile.getGame().equalsIgnoreCase(game)) {
				continue;
			}

			PlayerXP xp = playerXPRepo.findByPlayerId(profile.getId());
			long totalXp = (xp != null) ? xp.getTotalXp() : 0;

			Map<String, Object> row = new LinkedHashMap<>();
			row.put("playerId", profile.getId());
			row.put("xp", totalXp);
			row.put("level", (xp != null && xp.getCurrentLevel() > 0) ? xp.getCurrentLevel() : (int)(totalXp / 100) + 1);
			row.put("gamerTag", profile.getGamerTag() != null ? profile.getGamerTag() : "Unknown");
			row.put("game", profile.getGame());
			row.put("region", profile.getRegion());
			row.put("rankName", profile.getRankName());
			rows.add(row);
		}

		rows.sort((a, b) -> Long.compare((long) b.get("xp"), (long) a.get("xp")));

		List<Object> enriched = new ArrayList<>();
		int rank = 1;
		for (Map<String, Object> row : rows) {
			row.put("rank", rank++);
			enriched.add(row);
			if (enriched.size() >= topN) break;
		}
		return enriched;
	}

	public Map<String, Object> getXpRank(Long playerId, String game, String mode) {
		PlayerProfile profile = playerProfileRepo.findById(playerId).orElse(null);
		PlayerXP xp = playerXPRepo.findByPlayerId(playerId);

		long myXp = (xp != null) ? xp.getTotalXp() : 0;
		int myLevel = (xp != null && xp.getCurrentLevel() > 0) ? xp.getCurrentLevel() : (int)(myXp / 100) + 1;

		List<PlayerProfile> profiles = playerProfileRepo.findAll();
		int rank = 1;
		for (PlayerProfile p : profiles) {
			if (p.getId().equals(playerId)) continue;
			PlayerXP otherXp = playerXPRepo.findByPlayerId(p.getId());
			long other = (otherXp != null) ? otherXp.getTotalXp() : 0;
			if (other > myXp) rank++;
		}

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("playerId", playerId);
		response.put("xp", myXp);
		response.put("level", myLevel);
		response.put("rank", rank);
		response.put("gamerTag", profile != null ? profile.getGamerTag() : "Player");
		return response;
	}
}

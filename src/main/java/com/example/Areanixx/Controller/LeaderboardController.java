package com.example.Areanixx.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Areanixx.Service.LeaderboardService;

@RequestMapping("/leaderboard")
@RestController
public class LeaderboardController {

	@Autowired
	private LeaderboardService leaderboardService;

	// game dropdown: "all" or a specific game (Free Fire, Valorant...)
	// mode dropdown: "all", "solo", "duo", "team"
	@GetMapping("/xp")
	public ResponseEntity<?> getXpLeaderboard(
			@RequestParam(defaultValue = "all") String game,
			@RequestParam(defaultValue = "all") String mode,
			@RequestParam(defaultValue = "50") long topN) {
		return ResponseEntity.ok(leaderboardService.getEnrichedXpLeaderboard(game, mode, topN));
	}

	@GetMapping("/xp/rank/{playerId}")
	public ResponseEntity<?> getMyXpRank(@PathVariable Long playerId,
			@RequestParam(defaultValue = "all") String game,
			@RequestParam(defaultValue = "all") String mode) {
		return ResponseEntity.ok(leaderboardService.getXpRank(playerId, game, mode));
	}

}

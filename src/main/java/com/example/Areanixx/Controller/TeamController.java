package com.example.Areanixx.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Areanixx.Entity.Team;
import com.example.Areanixx.Service.TeamService;

@RequestMapping("/team")
@RestController
public class TeamController {

	@Autowired
	private TeamService ts;

	@PostMapping("/create")
	public ResponseEntity<String> createTeam(@RequestBody Team t) {
		ts.createTeam(t);
		return ResponseEntity.status(HttpStatus.CREATED).body("team created successfully!!!");
	}

	@PostMapping("/{teamId}/join-as-manager/{userId}")
	public ResponseEntity<String> joinAsManager(@PathVariable Long teamId, @PathVariable Long userId) {
		ts.requestCoManager(teamId, userId);
		return ResponseEntity.ok("co-manager request sent!!!");
	}

	@PostMapping("/{teamId}/addmember/{playerId}")
	public ResponseEntity<String> addMember(@PathVariable Long teamId, @PathVariable Long playerId) {
		ts.addMember(teamId, playerId);
		return ResponseEntity.ok("player added to team roster!!!");
	}

	@DeleteMapping("/{teamId}/removemember/{playerId}")
	public ResponseEntity<String> removeMember(@PathVariable Long teamId, @PathVariable Long playerId) {
		ts.removeMember(teamId, playerId);
		return ResponseEntity.ok("player removed from team roster!!!");
	}

	@GetMapping("/{teamId}/roster")
	public ResponseEntity<?> viewRoster(@PathVariable Long teamId) {
		return ResponseEntity.ok(ts.getRoster(teamId));
	}

	@GetMapping("/managed-by/{userId}")
	public List<Team> getManagedTeams(@PathVariable Long userId) {
		return ts.getManagedTeams(userId);
	}

	@GetMapping("/search")
	public List<Team> searchTeams(@RequestParam String query) {
		return ts.searchByName(query);
	}

	@GetMapping("/player/{playerId}")
	public ResponseEntity<?> getPlayerTeam(@PathVariable Long playerId) {
		return ResponseEntity.ok(ts.getPlayerTeamDetails(playerId));
	}
}

package com.example.Areanixx.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Areanixx.Entity.Tournament;
import com.example.Areanixx.Entity.TournamentRegistration;
import com.example.Areanixx.Service.TournamentService;

@RequestMapping("/tournament")
@RestController
public class TournamentController {

	@Autowired
	private TournamentService ts;

	@GetMapping("/live")
	public List<Tournament> getLiveTournaments() {
		return ts.getTournamentsByStatus(com.example.Areanixx.Entity.TournamentStatus.ONGOING);
	}

	@GetMapping("/status/{status}")
	public List<Tournament> getByStatus(@PathVariable com.example.Areanixx.Entity.TournamentStatus status) {
		return ts.getTournamentsByStatus(status);
	}

	@PostMapping("/create")
	public ResponseEntity<String> create(@RequestBody Tournament t) {
		ts.createTournament(t);
		return ResponseEntity.status(HttpStatus.CREATED).body("tournament created successfully!!!");
	}

	@GetMapping("/{id}/detail")
	public ResponseEntity<?> detail(@PathVariable Long id) {
		return ResponseEntity.ok(ts.getTournamentDetail(id));
	}

	@GetMapping("/{id}/registrations")
	public ResponseEntity<List<TournamentRegistration>> getTournamentRegistrations(@PathVariable Long id) {
		return ResponseEntity.ok(ts.getTournamentRegistrations(id));
	}

	@PatchMapping("/{id}/start")
	public ResponseEntity<?> startTournament(@PathVariable Long id) {
		return ResponseEntity.ok(ts.startTournament(id));
	}

	@PatchMapping("/{id}/release-room")
	public ResponseEntity<?> releaseRoom(@PathVariable Long id, @RequestParam String roomId, @RequestParam String roomPassword) {
		return ResponseEntity.ok(ts.releaseRoomDetails(id, roomId, roomPassword));
	}

	@PostMapping("/{id}/register")
	public ResponseEntity<String> register(@PathVariable Long id, @RequestParam(required=false) Long playerId, @RequestParam(required=false) Long teamId) {
		ts.registerForTournament(id, playerId, teamId);
		return ResponseEntity.status(HttpStatus.CREATED).body("registration submitted, pending approval!!!");
	}

	@GetMapping("/team/{teamId}/registrations")
	public ResponseEntity<List<TournamentRegistration>> getTeamRegistrations(@PathVariable Long teamId) {
		return ResponseEntity.ok(ts.getTeamRegistrations(teamId));
	}

	@PatchMapping("/registration/{regId}/approve")
	public ResponseEntity<?> approveRegistration(@PathVariable Long regId) {
		return ResponseEntity.ok(ts.approveRegistration(regId));
	}

	@PatchMapping("/registration/{regId}/reject")
	public ResponseEntity<?> rejectRegistration(@PathVariable Long regId) {
		return ResponseEntity.ok(ts.rejectRegistration(regId));
	}

	@PostMapping("/{id}/schedule-match")
	public ResponseEntity<String> scheduleMatch(@PathVariable Long id, @RequestParam int round,
			@RequestParam Long teamAId, @RequestParam Long teamBId,
			@RequestParam java.time.Instant scheduledTime) {
		ts.scheduleMatch(id, round, teamAId, teamBId, scheduledTime);
		return ResponseEntity.status(HttpStatus.CREATED).body("match scheduled successfully!!!");
	}

	@PostMapping("/{id}/enter-result")
	public ResponseEntity<String> enterResult(@PathVariable Long id, @RequestParam(required=false) Long playerId, @RequestParam(required=false) Long teamId, @RequestParam int placement) {
		ts.enterResult(id, playerId, teamId, placement);
		return ResponseEntity.status(HttpStatus.CREATED).body("result recorded successfully!!!");
	}

	@PatchMapping("/{id}/mark-prize-paid")
	public ResponseEntity<?> markPrizePaid(@PathVariable Long id) {
		return ResponseEntity.ok(ts.markPrizePaid(id));
	}

	@PatchMapping("/{id}/complete")
	public ResponseEntity<?> complete(@PathVariable Long id) {
		return ResponseEntity.ok(ts.completeTournament(id));
	}

	@GetMapping("/{id}/leaderboard")
	public ResponseEntity<?> leaderboard(@PathVariable Long id) {
		return ResponseEntity.ok(ts.getLiveLeaderboard(id));
	}

	@PostMapping("/{id}/report")
	public ResponseEntity<?> reportTournament(@PathVariable Long id, @RequestParam Long userId, @RequestParam String reason) {
		return ResponseEntity.status(HttpStatus.CREATED).body(ts.reportTournament(id, userId, reason));
	}

	@PostMapping("/{id}/dispute")
	public ResponseEntity<?> raiseDispute(@PathVariable Long id, @RequestParam Long userId, @RequestParam String description) {
		return ResponseEntity.status(HttpStatus.CREATED).body(ts.raiseDispute(id, userId, description));
	}
}

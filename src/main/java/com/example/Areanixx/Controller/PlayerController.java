package com.example.Areanixx.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Areanixx.Entity.PlayerProfile;
import com.example.Areanixx.Service.PlayerService;

@RequestMapping("/player")
@RestController
public class PlayerController {

	@Autowired
	private PlayerService ps;

	@PostMapping("/onboard")
	public ResponseEntity<String> onboard(@RequestBody PlayerProfile p) {
		ps.createProfile(p);
		return ResponseEntity.status(HttpStatus.CREATED).body("player profile created successfully!!!");
	}

	@GetMapping("/all")
	public ResponseEntity<List<PlayerProfile>> getAllPlayers() {
		return ResponseEntity.ok(ps.getAllPlayers());
	}

	@GetMapping("/getby/{id}")
	public ResponseEntity<?> getbyid(@PathVariable Long id){
		PlayerProfile data = ps.getbyid(id);
		if(data != null) return ResponseEntity.ok(data);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("player not found of this id!!");
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateProfile(@PathVariable long id, @RequestBody PlayerProfile p) {
		PlayerProfile updated = ps.patchprofile(id, p);
		if(updated != null) return ResponseEntity.ok(updated);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("player not found with the given id!!");
	}

	@PatchMapping("/patch/{id}")
	public ResponseEntity<?> patchprofile(@PathVariable long id, @RequestBody PlayerProfile p) {
		PlayerProfile updated = ps.patchprofile(id, p);
		if(updated != null) return ResponseEntity.ok(updated);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("player not found with the given id!!");
	}

	@PatchMapping("/{id}/image")
	public ResponseEntity<?> updateProfileImage(@PathVariable Long id, @RequestParam String imageUrl) {
		PlayerProfile updated = ps.updateProfileImage(id, imageUrl);
		if(updated != null) return ResponseEntity.ok(updated);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("player not found with the given id!!");
	}

	@PatchMapping("/{id}/availability")
	public ResponseEntity<?> setAvailability(@PathVariable Long id, @RequestParam String status) {
		return ResponseEntity.ok(ps.updateAvailability(id, status));
	}

	@GetMapping("/{id}/tournament-history")
	public ResponseEntity<?> tournamentHistory(@PathVariable Long id) {
		return ResponseEntity.ok(ps.getTournamentHistory(id));
	}

	@GetMapping("/{id}/achievements")
	public ResponseEntity<?> achievements(@PathVariable Long id) {
		return ResponseEntity.ok(ps.getAchievements(id));
	}

	@GetMapping("/{id}/statistics")
	public ResponseEntity<?> statistics(@PathVariable Long id) {
		return ResponseEntity.ok(ps.getStatistics(id));
	}

	@GetMapping("/{id}/registrations")
	public ResponseEntity<?> registrations(@PathVariable Long id) {
		return ResponseEntity.ok(ps.getRegistrations(id));
	}

	@PostMapping("/{id}/statistics")
	public ResponseEntity<?> recordStatistics(@PathVariable Long id, @RequestParam String game, @RequestBody String statSnapshotJson) {
		return ResponseEntity.status(HttpStatus.CREATED).body(ps.recordStatistics(id, game, statSnapshotJson));
	}

	@GetMapping("/live")
	public List<PlayerProfile> liveNow(@RequestParam(required = false) String game) {
		return ps.getLiveStreamers(game);
	}
}

package com.example.Areanixx.Controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Areanixx.Entity.Invite;
import com.example.Areanixx.Entity.RecruiterProfile;
import com.example.Areanixx.Entity.ShortlistedPlayer;
import com.example.Areanixx.Entity.Team;
import com.example.Areanixx.Service.RecruiterService;
import com.example.Areanixx.dto.PlayerSearchResultDto;

@RequestMapping("/recruiter")
@RestController
public class RecruiterController {

	@Autowired
	private RecruiterService rs;

	@PostMapping("/onboard")
	public ResponseEntity<String> onboard(@RequestBody RecruiterProfile r) {
		rs.createProfile(r);
		return ResponseEntity.status(HttpStatus.CREATED).body("recruiter profile created successfully!!!");
	}

	// PART 2 & PART 4: Search players with performance data DTO and pagination
	@GetMapping("/search")
	public ResponseEntity<Page<PlayerSearchResultDto>> searchPlayers(
			@RequestParam(required = false) String game,
			@RequestParam(required = false) String region,
			@RequestParam(required = false) String rank,
			@RequestParam(required = false) String roleInGame,
			@RequestParam(required = false) Integer minAge,
			@RequestParam(required = false) Integer maxAge,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "20") int size) {
		Page<PlayerSearchResultDto> results = rs.searchPlayers(game, region, rank, roleInGame, minAge, maxAge, page, size);
		return ResponseEntity.ok(results);
	}

	// Shortlist player
	@PostMapping("/{recruiterId}/shortlist/{playerId}")
	public ResponseEntity<String> shortlistPlayer(@PathVariable Long recruiterId, @PathVariable Long playerId) {
		rs.shortlistPlayer(recruiterId, playerId);
		return ResponseEntity.ok("player added to shortlist!!!");
	}

	// PART 3.1: Remove player from shortlist
	@DeleteMapping("/{recruiterId}/shortlist/{playerId}")
	public ResponseEntity<String> removeFromShortlist(@PathVariable Long recruiterId, @PathVariable Long playerId) {
		boolean removed = rs.removeFromShortlist(recruiterId, playerId);
		if (removed) {
			return ResponseEntity.ok("player removed from shortlist successfully!!!");
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Shortlisted player entry not found");
	}

	// Send join/recruitment invite to a player
	@PostMapping("/{recruiterId}/invite/{playerId}")
	public ResponseEntity<String> sendInvite(@PathVariable Long recruiterId, @PathVariable Long playerId) {
		rs.sendInvite(recruiterId, playerId);
		return ResponseEntity.status(HttpStatus.CREATED).body("invite sent successfully!!!");
	}

	// PART 3.2: Withdraw a sent invite
	@DeleteMapping("/invite/{inviteId}")
	public ResponseEntity<String> withdrawInvite(@PathVariable Long inviteId) {
		String result = rs.withdrawInvite(inviteId);
		if ("SUCCESS".equals(result)) {
			return ResponseEntity.ok("invite withdrawn successfully!!!");
		} else if ("NOT_FOUND".equals(result)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invite not found");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot withdraw invite with status: " + result.replace("CANNOT_WITHDRAW_", ""));
		}
	}

	// PART 3.3: View shortlist with notInvitedOnly filter
	@GetMapping("/{recruiterId}/shortlist")
	public ResponseEntity<List<ShortlistedPlayer>> viewShortlist(
			@PathVariable Long recruiterId,
			@RequestParam(defaultValue = "false") boolean notInvitedOnly) {
		return ResponseEntity.ok(rs.getShortlist(recruiterId, notInvitedOnly));
	}

	// Sent invites status tracker
	@GetMapping("/{recruiterId}/invites")
	public ResponseEntity<List<Invite>> viewInvites(@PathVariable Long recruiterId) {
		return ResponseEntity.ok(rs.getSentInvites(recruiterId));
	}

	// PART 3.4: Get managed team for recruiter
	@GetMapping("/{userId}/managed-team")
	public ResponseEntity<?> getManagedTeam(@PathVariable Long userId) {
		Team team = rs.getManagedTeam(userId);
		return ResponseEntity.ok(team);
	}

	// PART 3.5: OTP Verification flow
	@PostMapping("/{id}/send-otp")
	public ResponseEntity<String> sendEmailOtp(@PathVariable Long id) {
		boolean sent = rs.sendEmailOtp(id);
		if (sent) {
			return ResponseEntity.ok("OTP sent successfully to recruiter email!!!");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to send OTP. Verify recruiter profile and email exist.");
	}

	@PostMapping("/{id}/verify-otp")
	public ResponseEntity<?> verifyEmailOtp(@PathVariable Long id, @RequestParam String otp) {
		RecruiterProfile verified = rs.verifyEmailOtp(id, otp);
		if (verified != null) {
			return ResponseEntity.ok(verified);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired OTP");
	}

	// ==========================================
	// PLAYER-FACING RECRUITMENT ENDPOINTS
	// ==========================================

	@GetMapping("/player/{playerId}/invites")
	public ResponseEntity<List<Invite>> getPlayerInvites(@PathVariable Long playerId) {
		return ResponseEntity.ok(rs.getPlayerInvites(playerId));
	}

	@GetMapping("/player/{playerId}/shortlists")
	public ResponseEntity<List<ShortlistedPlayer>> getPlayerShortlists(@PathVariable Long playerId) {
		return ResponseEntity.ok(rs.getPlayerShortlists(playerId));
	}

	@GetMapping("/player/{playerId}/shortlist-count")
	public ResponseEntity<Map<String, Long>> getPlayerShortlistCount(@PathVariable Long playerId) {
		long count = rs.getPlayerShortlistCount(playerId);
		return ResponseEntity.ok(Collections.singletonMap("count", count));
	}

	@PostMapping("/invite/{inviteId}/accept")
	public ResponseEntity<?> acceptInvite(@PathVariable Long inviteId) {
		Invite accepted = rs.acceptInvite(inviteId);
		if (accepted != null) {
			return ResponseEntity.ok(accepted);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invite not found");
	}

	@PostMapping("/invite/{inviteId}/decline")
	public ResponseEntity<?> declineInvite(@PathVariable Long inviteId) {
		Invite declined = rs.declineInvite(inviteId);
		if (declined != null) {
			return ResponseEntity.ok(declined);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invite not found");
	}

	@GetMapping("/all")
	public ResponseEntity<List<RecruiterProfile>> getAllRecruiters() {
		return ResponseEntity.ok(rs.getAllRecruiters());
	}

	@PostMapping("/apply/{recruiterId}")
	public ResponseEntity<?> applyToRecruiter(@PathVariable Long recruiterId, @RequestParam Long playerId) {
		Invite application = rs.applyToRecruiter(recruiterId, playerId);
		return ResponseEntity.status(HttpStatus.CREATED).body(application);
	}

	@GetMapping("/getby/{userId}")
	public ResponseEntity<?> getByUserId(@PathVariable Long userId) {
		RecruiterProfile r = rs.getByUserId(userId);
		return ResponseEntity.ok(r);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {
		RecruiterProfile r = rs.getById(id);
		if (r != null) {
			return ResponseEntity.ok(r);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recruiter profile not found for id: " + id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody RecruiterProfile updated) {
		RecruiterProfile saved = rs.updateProfile(id, updated);
		if (saved != null) {
			return ResponseEntity.ok(saved);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recruiter profile not found for id: " + id);
	}

	@PatchMapping("/{id}/image")
	public ResponseEntity<?> updateRecruiterImage(@PathVariable Long id, @RequestParam String imageUrl) {
		RecruiterProfile existing = rs.getById(id);
		if (existing == null) {
			existing = rs.getByUserId(id);
		}
		if (existing != null) {
			existing.setLogoUrl(imageUrl);
			RecruiterProfile saved = rs.updateProfile(existing.getId(), existing);
			return ResponseEntity.ok(saved);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recruiter profile not found for id: " + id);
	}

}

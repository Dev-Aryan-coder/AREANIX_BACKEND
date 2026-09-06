package com.example.Areanixx.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Areanixx.Entity.Organizer;
import com.example.Areanixx.Entity.RecruiterProfile;
import com.example.Areanixx.Entity.User;
import com.example.Areanixx.Service.AdminService;


@RequestMapping("/admin-login")
@RestController
public class AdminController {

	@Autowired
	private AdminService as;

	// user management: list all, search, suspend, unsuspend
	@GetMapping("/users")
	public List<User> allUsers() {
		return as.getAllUsers();
	}

	@GetMapping("/users/search")
	public List<User> searchUsers(@RequestParam String query) {
		return as.searchUsers(query);
	}

	@PatchMapping("/users/{id}/suspend")
	public ResponseEntity<?> suspendUser(@PathVariable Long id) {
		return ResponseEntity.ok(as.suspendUser(id));
	}

	@PatchMapping("/users/{id}/unsuspend")
	public ResponseEntity<?> unsuspendUser(@PathVariable Long id) {
		return ResponseEntity.ok(as.unsuspendUser(id));
	}

	// organizer verification & full roster
	@GetMapping("/organizers")
	public List<Organizer> allOrganizers() {
		return as.getAllOrganizers();
	}

	@GetMapping("/organizers/pending")
	public List<Organizer> pendingOrganizers() {
		return as.getPendingOrganizers();
	}

	@PatchMapping("/organizers/{id}/approve")
	public ResponseEntity<?> approveOrganizer(@PathVariable Long id, @RequestParam(required=false) String reason) {
		return ResponseEntity.ok(as.approveOrganizer(id));
	}

	@PatchMapping("/organizers/{id}/reject")
	public ResponseEntity<?> rejectOrganizer(@PathVariable Long id, @RequestParam String reason) {
		return ResponseEntity.ok(as.rejectOrganizer(id, reason));
	}

	@PatchMapping("/organizers/{id}/verify-channel")
	public ResponseEntity<?> verifyChannel(@PathVariable Long id, @RequestParam boolean verified) {
		Organizer updated = as.verifyChannel(id, verified);
		if (updated != null) {
			return ResponseEntity.ok(updated);
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Organizer not found with id: " + id);
	}

	// recruiter verification & full roster
	@GetMapping("/recruiters")
	public List<RecruiterProfile> allRecruiters() {
		return as.getAllRecruiters();
	}

	@GetMapping("/recruiters/pending")
	public List<RecruiterProfile> pendingRecruiters() {
		return as.getPendingRecruiters();
	}

	@PatchMapping("/recruiters/{id}/approve")
	public ResponseEntity<?> approveRecruiter(@PathVariable Long id) {
		return ResponseEntity.ok(as.approveRecruiter(id));
	}

	@PatchMapping("/recruiters/{id}/reject")
	public ResponseEntity<?> rejectRecruiter(@PathVariable Long id, @RequestParam String reason) {
		return ResponseEntity.ok(as.rejectRecruiter(id, reason));
	}

	// flagged/reported tournaments & moderation
	@GetMapping("/tournaments/flagged")
	public ResponseEntity<?> flaggedTournaments() {
		return ResponseEntity.ok(as.getFlaggedTournaments());
	}

	@PatchMapping("/reports/{reportId}/review")
	public ResponseEntity<?> reviewReport(@PathVariable Long reportId) {
		return ResponseEntity.ok(as.reviewReport(reportId));
	}

	@PatchMapping("/tournaments/{id}/cancel")
	public ResponseEntity<?> cancelTournament(@PathVariable Long id) {
		return ResponseEntity.ok(as.cancelTournament(id));
	}

	// dispute resolution (prize payout complaints, fraud reports)
	@GetMapping("/disputes")
	public ResponseEntity<?> disputes() {
		return ResponseEntity.ok(as.getOpenDisputes());
	}

	@PatchMapping("/disputes/{disputeId}/resolve")
	public ResponseEntity<?> resolveDispute(@PathVariable Long disputeId) {
		return ResponseEntity.ok(as.resolveDispute(disputeId));
	}

	// rich platform-wide stats
	@GetMapping("/stats")
	public ResponseEntity<?> platformStats() {
		return ResponseEntity.ok(as.getPlatformStats());
	}
}

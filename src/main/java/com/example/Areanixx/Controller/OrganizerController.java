package com.example.Areanixx.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Areanixx.Entity.Dispute;
import com.example.Areanixx.Entity.Organizer;
import com.example.Areanixx.Entity.TournamentRegistration;
import com.example.Areanixx.Service.OrganizerService;
import com.example.Areanixx.dto.OrganizerDashboardDto;

@RequestMapping("/organizer")
@RestController
public class OrganizerController {

	@Autowired
	private OrganizerService os;

	// step 2 of organizer onboarding: phone + youtube channel submitted, status starts PENDING
	@PostMapping("/onboard")
	public ResponseEntity<String> onboard(@RequestBody Organizer o) {
		os.createOrganizer(o);
		return ResponseEntity.status(HttpStatus.CREATED).body("organizer verification submitted, status PENDING!!!");
	}

	// step 1: sends a real random OTP to the organizer's account email
	@PostMapping("/{id}/send-otp")
	public ResponseEntity<String> sendOtp(@PathVariable Long id) {
		os.sendEmailOtp(id);
		return ResponseEntity.ok("OTP sent to organizer's registered email!!!");
	}

	// step 2: organizer submits the OTP they received by email
	@PostMapping("/{id}/verify-otp")
	public ResponseEntity<?> verifyOtp(@PathVariable Long id, @RequestParam String otp) {
		return ResponseEntity.ok(os.verifyEmailOtp(id, otp));
	}

	@GetMapping("/{id}/status")
	public ResponseEntity<?> verificationStatus(@PathVariable Long id) {
		return ResponseEntity.ok(os.getVerificationStatus(id));
	}

	@GetMapping("/getby/{id}")
	public ResponseEntity<?> getbyid(@PathVariable Long id){
		Organizer data = os.getbyid(id);
		if(data != null) return ResponseEntity.ok(data);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("organizer not found of this id!!");
	}

	@GetMapping("/{id}/dashboard")
	public ResponseEntity<?> getDashboard(@PathVariable Long id) {
		OrganizerDashboardDto dto = os.getDashboard(id);
		if (dto != null) return ResponseEntity.ok(dto);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("organizer not found with id: " + id);
	}

	@PatchMapping("/{id}/profile")
	public ResponseEntity<?> updateProfile(@PathVariable Long id, @RequestBody Organizer updates) {
		Organizer updated = os.updateProfile(id, updates);
		if (updated != null) return ResponseEntity.ok(updated);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("organizer not found with id: " + id);
	}

	@PatchMapping("/dispute/{id}/resolve")
	public ResponseEntity<?> resolveDispute(@PathVariable Long id) {
		Dispute d = os.resolveDispute(id);
		if (d != null) return ResponseEntity.ok(d);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dispute not found with id: " + id);
	}

	@GetMapping("/{id}/disputes")
	public ResponseEntity<List<Dispute>> getDisputes(@PathVariable Long id) {
		return ResponseEntity.ok(os.getDisputesForOrganizer(id));
	}

	@GetMapping("/{id}/pending-registrations")
	public ResponseEntity<List<TournamentRegistration>> getPendingRegistrations(@PathVariable Long id) {
		return ResponseEntity.ok(os.getPendingRegistrations(id));
	}
}

package com.example.Areanixx.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Areanixx.Entity.Community;
import com.example.Areanixx.Service.CommunityService;

@RequestMapping("/community")
@RestController
public class CommunityController {

	@Autowired
	private CommunityService cs;

	// any role - Player/Recruiter/Organizer/Admin - can create
	@PostMapping("/create")
	public ResponseEntity<String> create(@RequestBody Community c) {
		cs.createCommunity(c);
		return ResponseEntity.status(HttpStatus.CREATED).body("community created successfully!!!");
	}

	@GetMapping("/search")
	public List<Community> search(@RequestParam String query) {
		return cs.searchByName(query);
	}

	// public communities: instant join. private: goes to request-pending state
	@PostMapping("/{communityId}/join/{userId}")
	public ResponseEntity<String> join(@PathVariable Long communityId, @PathVariable Long userId) {
		String result = cs.joinOrRequest(communityId, userId);
		return ResponseEntity.status(HttpStatus.CREATED).body(result);
	}

	@GetMapping("/{communityId}/members")
	public ResponseEntity<?> members(@PathVariable Long communityId) {
		return ResponseEntity.ok(cs.getMembers(communityId));
	}
}

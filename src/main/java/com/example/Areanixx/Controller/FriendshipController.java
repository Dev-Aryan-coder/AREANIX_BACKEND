package com.example.Areanixx.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Areanixx.Entity.Friendship;
import com.example.Areanixx.Service.FriendshipService;

@RequestMapping("/friendship")
@RestController
public class FriendshipController {

	@Autowired
	private FriendshipService fs;

	@PostMapping("/request")
	public ResponseEntity<?> sendRequest(@RequestParam Long fromUserId, @RequestParam Long toUserId) {
		return ResponseEntity.ok(fs.sendFriendRequest(fromUserId, toUserId));
	}

	@GetMapping("/requests/{userId}")
	public ResponseEntity<List<Friendship>> getPendingRequests(@PathVariable Long userId) {
		return ResponseEntity.ok(fs.getPendingRequests(userId));
	}

	@PostMapping("/accept/{id}")
	public ResponseEntity<?> acceptRequest(@PathVariable Long id) {
		return ResponseEntity.ok(fs.acceptRequest(id));
	}

	@PostMapping("/reject/{id}")
	public ResponseEntity<?> rejectRequest(@PathVariable Long id) {
		fs.rejectRequest(id);
		return ResponseEntity.ok("Friend request rejected");
	}

	@GetMapping("/friends/{userId}")
	public ResponseEntity<List<Friendship>> getFriends(@PathVariable Long userId) {
		return ResponseEntity.ok(fs.getFriends(userId));
	}
}

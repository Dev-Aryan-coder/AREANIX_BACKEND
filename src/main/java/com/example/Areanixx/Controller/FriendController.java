package com.example.Areanixx.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Areanixx.Service.FriendService;

@RequestMapping("/friend")
@RestController
public class FriendController {

	@Autowired
	private FriendService fs;

	// send request from search results ("Send Friend Request" button)
	@PostMapping("/request/{fromUserId}/{toUserId}")
	public ResponseEntity<String> sendRequest(@PathVariable Long fromUserId, @PathVariable Long toUserId) {
		fs.sendRequest(fromUserId, toUserId);
		return ResponseEntity.ok("friend request sent!!!");
	}

	@PatchMapping("/accept/{friendshipId}")
	public ResponseEntity<?> accept(@PathVariable Long friendshipId) {
		return ResponseEntity.ok(fs.acceptRequest(friendshipId));
	}

	@PatchMapping("/block/{friendshipId}")
	public ResponseEntity<?> block(@PathVariable Long friendshipId) {
		return ResponseEntity.ok(fs.blockUser(friendshipId));
	}

	// friend list shown in sidebar, with availability status alongside each friend
	@GetMapping("/{userId}/list")
	public ResponseEntity<?> friendList(@PathVariable Long userId) {
		return ResponseEntity.ok(fs.getFriendList(userId));
	}
}

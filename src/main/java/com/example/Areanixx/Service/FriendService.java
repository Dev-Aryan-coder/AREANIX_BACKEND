package com.example.Areanixx.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Areanixx.Entity.Friendship;
import com.example.Areanixx.Entity.FriendshipStatus;
import com.example.Areanixx.Repo.FriendshipRepo;

@Service
public class FriendService {

	@Autowired
	private FriendshipRepo friendshipRepo;

	public void sendRequest(Long fromUserId, Long toUserId) {
		Friendship f = new Friendship();
		f.setUserId1(fromUserId);
		f.setUserId2(toUserId);
		f.setStatus(FriendshipStatus.PENDING);
		friendshipRepo.save(f);
	}

	public Friendship acceptRequest(Long friendshipId) {
		Friendship f = friendshipRepo.findById(friendshipId).orElse(null);
		if (f == null) return null;
		f.setStatus(FriendshipStatus.ACCEPTED);
		return friendshipRepo.save(f);
	}

	public Friendship blockUser(Long friendshipId) {
		Friendship f = friendshipRepo.findById(friendshipId).orElse(null);
		if (f == null) return null;
		f.setStatus(FriendshipStatus.BLOCKED);
		return friendshipRepo.save(f);
	}

	public List<Friendship> getFriendList(Long userId) {
		return friendshipRepo.findAllForUserByStatus(userId, FriendshipStatus.ACCEPTED);
	}
}

package com.example.Areanixx.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Areanixx.Entity.Friendship;
import com.example.Areanixx.Entity.FriendshipStatus;
import com.example.Areanixx.Entity.PlayerProfile;
import com.example.Areanixx.Repo.FriendshipRepo;
import com.example.Areanixx.Repo.PlayerProfileRepo;
import com.example.Areanixx.Repo.UserRepo;

@Service
public class FriendshipService {

	@Autowired
	private FriendshipRepo friendshipRepo;

	@Autowired
	private PlayerProfileRepo playerRepo;

	@Autowired
	private UserRepo userRepo;

	public Friendship sendFriendRequest(Long fromUserId, Long toUserId) {
		// 1. Resolve fromUserId if passed as PlayerProfile ID
		Long actualFrom = fromUserId;
		if (actualFrom != null && userRepo.findById(actualFrom).isEmpty()) {
			PlayerProfile pp = playerRepo.findById(actualFrom).orElse(null);
			if (pp != null && pp.getUserId() != null) {
				actualFrom = pp.getUserId();
			}
		}

		// 2. Resolve toUserId if passed as PlayerProfile ID
		Long actualTo = toUserId;
		if (actualTo != null && userRepo.findById(actualTo).isEmpty()) {
			PlayerProfile pp = playerRepo.findById(actualTo).orElse(null);
			if (pp != null && pp.getUserId() != null) {
				actualTo = pp.getUserId();
			}
		}

		// Check if friendship or reverse friendship already exists
		List<Friendship> existing1 = friendshipRepo.findByUserId1AndUserId2(actualFrom, actualTo);
		if (existing1 != null && !existing1.isEmpty()) {
			Friendship f = existing1.get(0);
			if (f.getStatus() != FriendshipStatus.ACCEPTED) {
				f.setStatus(FriendshipStatus.PENDING);
				return friendshipRepo.save(f);
			}
			return f;
		}

		List<Friendship> existing2 = friendshipRepo.findByUserId1AndUserId2(actualTo, actualFrom);
		if (existing2 != null && !existing2.isEmpty()) {
			Friendship f = existing2.get(0);
			if (f.getStatus() != FriendshipStatus.ACCEPTED) {
				f.setUserId1(actualFrom);
				f.setUserId2(actualTo);
				f.setStatus(FriendshipStatus.PENDING);
				return friendshipRepo.save(f);
			}
			return f;
		}

		Friendship f = new Friendship();
		f.setUserId1(actualFrom);
		f.setUserId2(actualTo);
		f.setStatus(FriendshipStatus.PENDING);
		return friendshipRepo.save(f);
	}

	public List<Friendship> getPendingRequests(Long userId) {
		Long actualUser = userId;
		if (actualUser != null && userRepo.findById(actualUser).isEmpty()) {
			PlayerProfile pp = playerRepo.findById(actualUser).orElse(null);
			if (pp != null && pp.getUserId() != null) {
				actualUser = pp.getUserId();
			}
		}
		return friendshipRepo.findByUserId2AndStatus(actualUser, FriendshipStatus.PENDING);
	}

	public Friendship acceptRequest(Long id) {
		Friendship f = friendshipRepo.findById(id).orElse(null);
		if (f != null) {
			f.setStatus(FriendshipStatus.ACCEPTED);
			return friendshipRepo.save(f);
		}
		return null;
	}

	public void rejectRequest(Long id) {
		friendshipRepo.deleteById(id);
	}

	public List<Friendship> getFriends(Long userId) {
		Long actualUser = userId;
		if (actualUser != null && userRepo.findById(actualUser).isEmpty()) {
			PlayerProfile pp = playerRepo.findById(actualUser).orElse(null);
			if (pp != null && pp.getUserId() != null) {
				actualUser = pp.getUserId();
			}
		}
		return friendshipRepo.findAllForUserByStatus(actualUser, FriendshipStatus.ACCEPTED);
	}
}

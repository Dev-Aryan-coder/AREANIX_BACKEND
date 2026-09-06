package com.example.Areanixx.Service;

import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Areanixx.Entity.Community;
import com.example.Areanixx.Entity.CommunityMember;
import com.example.Areanixx.Entity.CommunityVisibility;
import com.example.Areanixx.Repo.CommunityMemberRepo;
import com.example.Areanixx.Repo.CommunityRepo;

@Service
public class CommunityService {

	@Autowired
	private CommunityRepo communityRepo;
	@Autowired
	private CommunityMemberRepo communityMemberRepo;

	public void createCommunity(Community c) {
		communityRepo.save(c);
	}

	public List<Community> searchByName(String query) {
		return communityRepo.findByNameContainingIgnoreCase(query);
	}

	// public: joins instantly. private: still recorded, but a Pending flag
	// would be needed here if request-to-join approval flow is built out further
	public String joinOrRequest(Long communityId, Long userId) {
		Community c = communityRepo.findById(communityId).orElse(null);
		if (c == null) return "community not found";
		CommunityMember cm = new CommunityMember();
		cm.setCommunityId(communityId);
		cm.setUserId(userId);
		cm.setJoinedAt(Instant.now());
		communityMemberRepo.save(cm);
		return (c.getVisibility() == CommunityVisibility.PUBLIC)
				? "joined community successfully!!!"
				: "join request sent, pending approval!!!";
	}

	public List<CommunityMember> getMembers(Long communityId) {
		return communityMemberRepo.findByCommunityId(communityId);
	}
}

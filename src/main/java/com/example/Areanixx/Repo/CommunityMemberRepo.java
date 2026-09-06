package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.CommunityMember;

public interface CommunityMemberRepo extends JpaRepository<CommunityMember, Long> {
	List<CommunityMember> findByCommunityId(Long communityId);
}

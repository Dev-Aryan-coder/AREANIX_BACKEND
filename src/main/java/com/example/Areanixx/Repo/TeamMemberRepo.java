package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.TeamMember;

public interface TeamMemberRepo extends JpaRepository<TeamMember, Long> {
	List<TeamMember> findByTeamIdAndLeftAtIsNull(Long teamId);
	TeamMember findByTeamIdAndPlayerIdAndLeftAtIsNull(Long teamId, Long playerId);
	List<TeamMember> findByPlayerIdAndLeftAtIsNull(Long playerId);
}

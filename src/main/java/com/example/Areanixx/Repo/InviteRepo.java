package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.Invite;
import com.example.Areanixx.Entity.InviteStatus;

public interface InviteRepo extends JpaRepository<Invite, Long> {
	List<Invite> findByRecruiterId(Long recruiterId);
	List<Invite> findByPlayerId(Long playerId);
	List<Invite> findByPlayerIdAndStatus(Long playerId, InviteStatus status);
}

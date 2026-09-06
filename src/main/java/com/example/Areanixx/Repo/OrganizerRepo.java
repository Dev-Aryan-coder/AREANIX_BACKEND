package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.Organizer;
import com.example.Areanixx.Entity.VerificationStatus;

public interface OrganizerRepo extends JpaRepository<Organizer, Long> {
	Organizer findByUserId(Long userId);
	List<Organizer> findByVerificationStatus(VerificationStatus status);
}

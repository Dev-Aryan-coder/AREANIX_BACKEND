package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.RecruiterProfile;
import com.example.Areanixx.Entity.VerificationStatus;

public interface RecruiterProfileRepo extends JpaRepository<RecruiterProfile, Long> {
	RecruiterProfile findByUserId(Long userId);
	List<RecruiterProfile> findByVerificationStatus(VerificationStatus status);
}

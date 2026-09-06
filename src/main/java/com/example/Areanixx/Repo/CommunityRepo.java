package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.Community;

public interface CommunityRepo extends JpaRepository<Community, Long> {
	List<Community> findByNameContainingIgnoreCase(String query);
}

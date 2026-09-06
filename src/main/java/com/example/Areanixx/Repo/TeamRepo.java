package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.Team;

public interface TeamRepo extends JpaRepository<Team, Long> {
	List<Team> findByNameContainingIgnoreCase(String query);
	List<Team> findByManagerId(Long managerId);
}

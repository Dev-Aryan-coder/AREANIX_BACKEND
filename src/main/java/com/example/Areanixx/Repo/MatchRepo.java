package com.example.Areanixx.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.Match;

public interface MatchRepo extends JpaRepository<Match, Long> {
}

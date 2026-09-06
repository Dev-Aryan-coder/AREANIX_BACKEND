package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.Areanixx.Entity.PlayerProfile;

public interface PlayerProfileRepo extends JpaRepository<PlayerProfile, Long> {
	PlayerProfile findByUserId(Long userId);

	@Query("SELECT p FROM PlayerProfile p WHERE " +
		"(:game IS NULL OR p.game = :game) AND " +
		"(:region IS NULL OR p.region = :region) AND " +
		"(:rank IS NULL OR p.rankName = :rank) AND " +
		"(:roleInGame IS NULL OR p.roleInGame = :roleInGame) AND " +
		"(:minAge IS NULL OR p.age >= :minAge) AND " +
		"(:maxAge IS NULL OR p.age <= :maxAge)")
	Page<PlayerProfile> searchPlayers(
			@Param("game") String game,
			@Param("region") String region,
			@Param("rank") String rank,
			@Param("roleInGame") String roleInGame,
			@Param("minAge") Integer minAge,
			@Param("maxAge") Integer maxAge,
			Pageable pageable);

	List<PlayerProfile> findByGame(String game);
}

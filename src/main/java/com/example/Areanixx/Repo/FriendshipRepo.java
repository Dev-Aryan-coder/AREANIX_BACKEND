package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.Areanixx.Entity.FriendshipStatus;
import com.example.Areanixx.Entity.Friendship;

public interface FriendshipRepo extends JpaRepository<Friendship, Long> {
	@Query("SELECT f FROM Friendship f WHERE (f.userId1 = :userId OR f.userId2 = :userId) AND f.status = :status")
	List<Friendship> findAllForUserByStatus(Long userId, FriendshipStatus status);

	List<Friendship> findByUserId2AndStatus(Long userId2, FriendshipStatus status);

	List<Friendship> findByUserId1AndUserId2(Long userId1, Long userId2);
}

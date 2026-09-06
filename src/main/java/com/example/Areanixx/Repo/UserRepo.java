package com.example.Areanixx.Repo;

import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.example.Areanixx.Entity.User;

public interface UserRepo extends JpaRepository<User, Long> {
	@Query("SELECT u FROM User u WHERE u.fullname LIKE %:query%")
	List<User> searchByNameOrTag(String query);

	User findByEmail(String email);

	long countByCreatedAtAfter(Instant since);
}

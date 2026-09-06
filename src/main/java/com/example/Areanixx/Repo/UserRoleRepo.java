package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.UserRole;

public interface UserRoleRepo extends JpaRepository<UserRole, Long> {
	List<UserRole> findByUserId(Long userId);
}

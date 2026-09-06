package com.example.Areanixx.Repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.XPTransaction;

public interface XPTransactionRepo extends JpaRepository<XPTransaction, Long> {
	List<XPTransaction> findByPlayerId(Long playerId);
}

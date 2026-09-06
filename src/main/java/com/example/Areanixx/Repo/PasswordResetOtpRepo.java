package com.example.Areanixx.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Areanixx.Entity.PasswordResetOtp;

public interface PasswordResetOtpRepo extends JpaRepository<PasswordResetOtp, Long> {
	PasswordResetOtp findByUserIdAndOtp(Long userId, String otp);
}

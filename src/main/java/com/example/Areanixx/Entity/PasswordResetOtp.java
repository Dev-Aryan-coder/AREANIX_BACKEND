package com.example.Areanixx.Entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class PasswordResetOtp {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getUserId() { return userId; }
	public void setUserId(Long userId) { this.userId = userId; }
	public String getOtp() { return otp; }
	public void setOtp(String otp) { this.otp = otp; }
	public Instant getExpiresAt() { return expiresAt; }
	public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
	public Boolean getVerified() { return verified; }
	public void setVerified(Boolean verified) { this.verified = verified; }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "User id is required....")
	private Long userId;

	@NotNull(message = "OTP is required....")
	private String otp;

	private Instant expiresAt;

	// true only after /verify-otp succeeds - /reset-password checks this,
	// not the raw otp again, so the otp can't be reused after verification
	private Boolean verified = false;

}

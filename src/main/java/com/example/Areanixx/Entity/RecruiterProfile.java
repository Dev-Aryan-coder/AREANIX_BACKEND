package com.example.Areanixx.Entity;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class RecruiterProfile {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getUserId() { return userId; }
	public void setUserId(Long userId) { this.userId = userId; }
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	public String getOrganizationName() { return organizationName; }
	public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
	public String getRegion() { return region; }
	public void setRegion(String region) { this.region = region; }
	public String getWebsiteUrl() { return websiteUrl; }
	public void setWebsiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; }
	public String getLogoUrl() { return logoUrl; }
	public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
	public String getGamesRecruiting() { return gamesRecruiting; }
	public void setGamesRecruiting(String gamesRecruiting) { this.gamesRecruiting = gamesRecruiting; }
	public String getBio() { return bio; }
	public void setBio(String bio) { this.bio = bio; }
	public VerificationStatus getVerificationStatus() { return verificationStatus; }
	public void setVerificationStatus(VerificationStatus verificationStatus) { this.verificationStatus = verificationStatus; }
	public String getRejectionReason() { return rejectionReason; }
	public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
	public String getEmailOtp() { return emailOtp; }
	public void setEmailOtp(String emailOtp) { this.emailOtp = emailOtp; }
	public Instant getOtpExpiresAt() { return otpExpiresAt; }
	public void setOtpExpiresAt(Instant otpExpiresAt) { this.otpExpiresAt = otpExpiresAt; }
	public Boolean getEmailVerified() { return emailVerified; }
	public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "User id is required....")
	@Column(name = "user_id", unique = true)
	private Long userId;

	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User user;

	@NotNull(message = "Organization name is required....")
	@Size(min = 3, max = 25, message = "give organization name between 3 to 25 characters..Pls")
	private String organizationName;

	@NotNull(message = "Region is required....")
	private String region;

	private String websiteUrl;

	private String logoUrl;

	private String gamesRecruiting;

	@Size(max = 500, message = "Bio must be at most 500 characters..Pls")
	private String bio;

	@Enumerated(EnumType.STRING)
	private VerificationStatus verificationStatus = VerificationStatus.PENDING;

	private String rejectionReason;

	@JsonIgnore
	private String emailOtp;

	@JsonIgnore
	private Instant otpExpiresAt;

	private Boolean emailVerified = false;

}

package com.example.Areanixx.Entity;

import java.time.Instant;
import java.util.List;

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
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Organizer {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getUserId() { return userId; }
	public void setUserId(Long userId) { this.userId = userId; }
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	public String getPhoneNumber() { return phoneNumber; }
	public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	public Boolean getPhoneVerified() { return phoneVerified; }
	public void setPhoneVerified(Boolean phoneVerified) { this.phoneVerified = phoneVerified; }
	public String getEmailOtp() { return emailOtp; }
	public void setEmailOtp(String emailOtp) { this.emailOtp = emailOtp; }
	public Instant getOtpExpiresAt() { return otpExpiresAt; }
	public void setOtpExpiresAt(Instant otpExpiresAt) { this.otpExpiresAt = otpExpiresAt; }
	public Boolean getEmailVerified() { return emailVerified; }
	public void setEmailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; }
	public String getYoutubeChannelUrl() { return youtubeChannelUrl; }
	public void setYoutubeChannelUrl(String youtubeChannelUrl) { this.youtubeChannelUrl = youtubeChannelUrl; }
	public Boolean getChannelVerified() { return channelVerified; }
	public void setChannelVerified(Boolean channelVerified) { this.channelVerified = channelVerified; }
	public VerificationStatus getVerificationStatus() { return verificationStatus; }
	public void setVerificationStatus(VerificationStatus verificationStatus) { this.verificationStatus = verificationStatus; }
	public String getRejectionReason() { return rejectionReason; }
	public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
	public List<Tournament> getTournaments() { return tournaments; }
	public void setTournaments(List<Tournament> tournaments) { this.tournaments = tournaments; }

	public String getOrganizationName() { return organizationName; }
	public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
	public String getLogoUrl() { return logoUrl; }
	public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
	public String getBannerUrl() { return bannerUrl; }
	public void setBannerUrl(String bannerUrl) { this.bannerUrl = bannerUrl; }
	public String getBio() { return bio; }
	public void setBio(String bio) { this.bio = bio; }
	public Long getSubscriberCount() { return subscriberCount; }
	public void setSubscriberCount(Long subscriberCount) { this.subscriberCount = subscriberCount; }
	public String getDiscordUrl() { return discordUrl; }
	public void setDiscordUrl(String discordUrl) { this.discordUrl = discordUrl; }
	public String getTwitterUrl() { return twitterUrl; }
	public void setTwitterUrl(String twitterUrl) { this.twitterUrl = twitterUrl; }
	public String getTwitchUrl() { return twitchUrl; }
	public void setTwitchUrl(String twitchUrl) { this.twitchUrl = twitchUrl; }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "User id is required....")
	@Column(name = "user_id", unique = true)
	private Long userId;

	@OneToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User user;

	@Column(name = "phone_number")
	private String phoneNumber;

	private Boolean phoneVerified = false;

	@JsonIgnore
	private String emailOtp;

	@JsonIgnore
	private Instant otpExpiresAt;

	private Boolean emailVerified = false;

	@NotNull(message = "Youtube channel url is required....")
	private String youtubeChannelUrl;

	private Boolean channelVerified = false;

	@Enumerated(EnumType.STRING)
	private VerificationStatus verificationStatus = VerificationStatus.PENDING;

	private String rejectionReason;

	@Size(max = 50, message = "Organization name must be at most 50 characters")
	private String organizationName;

	private String logoUrl;
	private String bannerUrl;

	@Size(max = 500, message = "Bio must be at most 500 characters")
	private String bio;

	private Long subscriberCount;
	private String discordUrl;
	private String twitterUrl;
	private String twitchUrl;

	@JsonIgnore
	@OneToMany(mappedBy = "organizer")
	private List<Tournament> tournaments;

}

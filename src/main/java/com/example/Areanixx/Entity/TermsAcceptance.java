package com.example.Areanixx.Entity;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;

@Entity
public class TermsAcceptance {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getUserId() { return userId; }
	public void setUserId(Long userId) { this.userId = userId; }
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	public String getTermsVersion() { return termsVersion; }
	public void setTermsVersion(String termsVersion) { this.termsVersion = termsVersion; }
	public Instant getAcceptedAt() { return acceptedAt; }
	public void setAcceptedAt(Instant acceptedAt) { this.acceptedAt = acceptedAt; }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "User id is required....")
	@Column(name = "user_id")
	private Long userId;

	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User user;

	@NotNull(message = "Terms version is required....")
	private String termsVersion;

	private Instant acceptedAt;

	@PrePersist
	protected void onCreate() {
		this.acceptedAt = Instant.now();
	}

}

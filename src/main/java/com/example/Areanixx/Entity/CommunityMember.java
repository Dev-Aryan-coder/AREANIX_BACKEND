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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"communityId", "userId"}))
public class CommunityMember {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getCommunityId() { return communityId; }
	public void setCommunityId(Long communityId) { this.communityId = communityId; }
	public Community getCommunity() { return community; }
	public void setCommunity(Community community) { this.community = community; }
	public Long getUserId() { return userId; }
	public void setUserId(Long userId) { this.userId = userId; }
	public User getUser() { return user; }
	public void setUser(User user) { this.user = user; }
	public Instant getJoinedAt() { return joinedAt; }
	public void setJoinedAt(Instant joinedAt) { this.joinedAt = joinedAt; }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Community id is required....")
	@Column(name = "community_id")
	private Long communityId;

	@ManyToOne
	@JoinColumn(name = "community_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Community community;

	@NotNull(message = "User id is required....")
	@Column(name = "user_id")
	private Long userId;

	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User user;

	private Instant joinedAt;

	@PrePersist
	protected void onCreate() {
		this.joinedAt = Instant.now();
	}

}

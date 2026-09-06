package com.example.Areanixx.Entity;

import java.time.Instant;

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
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"userId1", "userId2"}))
public class Friendship {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getUserId1() { return userId1; }
	public void setUserId1(Long userId1) { this.userId1 = userId1; }
	public User getUser1() { return user1; }
	public void setUser1(User user1) { this.user1 = user1; }
	public Long getUserId2() { return userId2; }
	public void setUserId2(Long userId2) { this.userId2 = userId2; }
	public User getUser2() { return user2; }
	public void setUser2(User user2) { this.user2 = user2; }
	public FriendshipStatus getStatus() { return status; }
	public void setStatus(FriendshipStatus status) { this.status = status; }
	public Instant getCreatedAt() { return createdAt; }
	public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "User id 1 is required....")
	@Column(name = "user_id1")
	private Long userId1;

	@ManyToOne
	@JoinColumn(name = "user_id1", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User user1;

	@NotNull(message = "User id 2 is required....")
	@Column(name = "user_id2")
	private Long userId2;

	@ManyToOne
	@JoinColumn(name = "user_id2", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User user2;

	@Enumerated(EnumType.STRING)
	private FriendshipStatus status = FriendshipStatus.PENDING;

	private Instant createdAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = Instant.now();
	}

}

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
import jakarta.validation.constraints.NotNull;

@Entity
public class XPTransaction {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public Long getPlayerId() { return playerId; }
	public void setPlayerId(Long playerId) { this.playerId = playerId; }
	public PlayerProfile getPlayer() { return player; }
	public void setPlayer(PlayerProfile player) { this.player = player; }
	public XPSource getSource() { return source; }
	public void setSource(XPSource source) { this.source = source; }
	public long getAmount() { return amount; }
	public void setAmount(long amount) { this.amount = amount; }
	public Long getReferenceId() { return referenceId; }
	public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
	public Instant getCreatedAt() { return createdAt; }
	public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Player id is required....")
	@Column(name = "player_id")
	private Long playerId;

	@ManyToOne
	@JoinColumn(name = "player_id", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private PlayerProfile player;

	@Enumerated(EnumType.STRING)
	private XPSource source;

	private long amount;

	// nullable - e.g. tournament id if source is TOURNAMENT_PLAY/PLACEMENT
	private Long referenceId;

	private Instant createdAt;

	@PrePersist
	protected void onCreate() {
		this.createdAt = Instant.now();
	}

}

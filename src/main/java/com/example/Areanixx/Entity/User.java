package com.example.Areanixx.Entity;

import java.time.Instant;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class User {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getFullname() { return fullname; }
	public void setFullname(String fullname) { this.fullname = fullname; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public String getGoogleAccountId() { return googleAccountId; }
	public void setGoogleAccountId(String googleAccountId) { this.googleAccountId = googleAccountId; }
	public Role getRole() { return role; }
	public void setRole(Role role) { this.role = role; }
	public Boolean getActive() { return active; }
	public void setActive(Boolean active) { this.active = active; }
	public Instant getCreatedAt() { return createdAt; }
	public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
	public Instant getLastModifiedAt() { return lastModifiedAt; }
	public void setLastModifiedAt(Instant lastModifiedAt) { this.lastModifiedAt = lastModifiedAt; }

	public List<Community> getCommunitiesCreated() { return communitiesCreated; }
	public void setCommunitiesCreated(List<Community> communitiesCreated) { this.communitiesCreated = communitiesCreated; }
	public List<CommunityMember> getCommunityMemberships() { return communityMemberships; }
	public void setCommunityMemberships(List<CommunityMember> communityMemberships) { this.communityMemberships = communityMemberships; }
	public List<Friendship> getFriendshipsInitiated() { return friendshipsInitiated; }
	public void setFriendshipsInitiated(List<Friendship> friendshipsInitiated) { this.friendshipsInitiated = friendshipsInitiated; }
	public List<Friendship> getFriendshipsReceived() { return friendshipsReceived; }
	public void setFriendshipsReceived(List<Friendship> friendshipsReceived) { this.friendshipsReceived = friendshipsReceived; }
	public Organizer getOrganizerProfile() { return organizerProfile; }
	public void setOrganizerProfile(Organizer organizerProfile) { this.organizerProfile = organizerProfile; }
	public PlayerProfile getPlayerProfile() { return playerProfile; }
	public void setPlayerProfile(PlayerProfile playerProfile) { this.playerProfile = playerProfile; }
	public RecruiterProfile getRecruiterProfile() { return recruiterProfile; }
	public void setRecruiterProfile(RecruiterProfile recruiterProfile) { this.recruiterProfile = recruiterProfile; }
	public List<TermsAcceptance> getTermsAccepted() { return termsAccepted; }
	public void setTermsAccepted(List<TermsAcceptance> termsAccepted) { this.termsAccepted = termsAccepted; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public List<Team> getTeamsManaged() { return teamsManaged; }
	public void setTeamsManaged(List<Team> teamsManaged) { this.teamsManaged = teamsManaged; }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Fullname is required....")
	@Size(min = 1, max = 25, message = "give user name between 1 to 25 characters..Pls")
	private String fullname;

	@Column(unique = true)
	private String email;

	@Column(unique = true)
	private String googleAccountId;

	@Enumerated(EnumType.STRING)
	private Role role = Role.PLAYER;

	@Column(nullable = false, columnDefinition = "boolean default true")
	private Boolean active = true;

	@CreatedDate
	private Instant createdAt;
	@LastModifiedDate
	private Instant lastModifiedAt;

	@JsonIgnore
	@OneToMany(mappedBy = "creator")
	private List<Community> communitiesCreated;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<CommunityMember> communityMemberships;

	@JsonIgnore
	@OneToMany(mappedBy = "user1")
	private List<Friendship> friendshipsInitiated;

	@JsonIgnore
	@OneToMany(mappedBy = "user2")
	private List<Friendship> friendshipsReceived;

	@JsonIgnore
	@OneToOne(mappedBy = "user")
	private Organizer organizerProfile;

	@JsonIgnore
	@OneToOne(mappedBy = "user")
	private PlayerProfile playerProfile;

	@JsonIgnore
	@OneToOne(mappedBy = "user")
	private RecruiterProfile recruiterProfile;

	@JsonIgnore
	@OneToMany(mappedBy = "user")
	private List<TermsAcceptance> termsAccepted;

	// nullable - only set for email/password fallback signup (Google OAuth users have no password)
	private String password;

	@JsonIgnore
	@OneToMany(mappedBy = "manager")
	private List<Team> teamsManaged;

}

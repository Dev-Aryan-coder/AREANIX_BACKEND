package com.example.Areanixx.Entity;

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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Community {

	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public String getBannerUrl() { return bannerUrl; }
	public void setBannerUrl(String bannerUrl) { this.bannerUrl = bannerUrl; }
	public Long getCreatedBy() { return createdBy; }
	public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
	public User getCreator() { return creator; }
	public void setCreator(User creator) { this.creator = creator; }
	public CommunityVisibility getVisibility() { return visibility; }
	public void setVisibility(CommunityVisibility visibility) { this.visibility = visibility; }
	public List<CommunityMember> getMembers() { return members; }
	public void setMembers(List<CommunityMember> members) { this.members = members; }

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Community name is required....")
	@Size(min = 3, max = 50, message = "give community name between 3 to 50 characters..Pls")
	private String name;

	@Lob
	private String description;

	private String bannerUrl;

	// any role (Player/Recruiter/Organizer/Admin) can create a community
	@NotNull(message = "Created by user id is required....")
	@Column(name = "created_by")
	private Long createdBy;

	@ManyToOne
	@JoinColumn(name = "created_by", insertable = false, updatable = false)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User creator;

	@Enumerated(EnumType.STRING)
	private CommunityVisibility visibility = CommunityVisibility.PUBLIC;

	@JsonIgnore
	@OneToMany(mappedBy = "community")
	private List<CommunityMember> members;

}

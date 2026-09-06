package com.example.Areanixx.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.Areanixx.Entity.Achievement;
import com.example.Areanixx.Entity.Invite;
import com.example.Areanixx.Entity.InviteStatus;
import com.example.Areanixx.Entity.PlayerProfile;
import com.example.Areanixx.Entity.PlayerXP;
import com.example.Areanixx.Entity.RecruiterProfile;
import com.example.Areanixx.Entity.ShortlistedPlayer;
import com.example.Areanixx.Entity.Team;
import com.example.Areanixx.Entity.TeamMember;
import com.example.Areanixx.Entity.User;
import com.example.Areanixx.Entity.VerificationStatus;
import com.example.Areanixx.Repo.AchievementRepo;
import com.example.Areanixx.Repo.InviteRepo;
import com.example.Areanixx.Repo.PlayerProfileRepo;
import com.example.Areanixx.Repo.PlayerXPRepo;
import com.example.Areanixx.Repo.RecruiterProfileRepo;
import com.example.Areanixx.Repo.ShortlistedPlayerRepo;
import com.example.Areanixx.Repo.TeamRepo;
import com.example.Areanixx.Repo.TeamMemberRepo;
import com.example.Areanixx.Repo.UserRepo;
import com.example.Areanixx.dto.PlayerSearchResultDto;

@Service
public class RecruiterService {

	@Autowired
	private RecruiterProfileRepo recruiterRepo;

	@Autowired
	private PlayerProfileRepo playerRepo;

	@Autowired
	private ShortlistedPlayerRepo shortlistRepo;

	@Autowired
	private InviteRepo inviteRepo;

	@Autowired
	private PlayerXPRepo playerXPRepo;

	@Autowired
	private AchievementRepo achievementRepo;

	@Autowired
	private TeamRepo teamRepo;

	@Autowired
	private TeamMemberRepo teamMemberRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private EmailService emailService;

	public void createProfile(RecruiterProfile r) {
		RecruiterProfile existing = null;
		if (r.getUserId() != null) {
			existing = recruiterRepo.findByUserId(r.getUserId());
		}
		if (existing != null) {
			if (r.getOrganizationName() != null) existing.setOrganizationName(r.getOrganizationName());
			if (r.getRegion() != null) existing.setRegion(r.getRegion());
			if (r.getWebsiteUrl() != null) existing.setWebsiteUrl(r.getWebsiteUrl());
			if (r.getLogoUrl() != null) existing.setLogoUrl(r.getLogoUrl());
			if (r.getGamesRecruiting() != null) existing.setGamesRecruiting(r.getGamesRecruiting());
			if (r.getBio() != null) existing.setBio(r.getBio());
			recruiterRepo.save(existing);
		} else {
			r.setVerificationStatus(VerificationStatus.PENDING);
			recruiterRepo.save(r);
		}
	}

	// PART 2 & PART 4: Search players with performance metrics and pagination
	public Page<PlayerSearchResultDto> searchPlayers(
			String game, String region, String rank, String roleInGame,
			Integer minAge, Integer maxAge, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<PlayerProfile> playersPage = playerRepo.searchPlayers(game, region, rank, roleInGame, minAge, maxAge, pageable);

		return playersPage.map(p -> {
			PlayerSearchResultDto dto = new PlayerSearchResultDto();
			dto.setId(p.getId());
			dto.setUserId(p.getUserId());
			dto.setGamerTag(p.getGamerTag());
			dto.setGame(p.getGame());
			dto.setRankName(p.getRankName());
			dto.setRoleInGame(p.getRoleInGame());
			dto.setRegion(p.getRegion());
			dto.setAge(p.getAge());
			dto.setTwitchUrl(p.getTwitchUrl());
			dto.setYoutubeUrl(p.getYoutubeUrl());
			dto.setProfileImageUrl(p.getProfileImageUrl());
			dto.setAvailabilityStatus(p.getAvailabilityStatus() != null ? p.getAvailabilityStatus().name() : null);

			// Total XP & Current Level
			PlayerXP pxp = playerXPRepo.findByPlayerId(p.getId());
			if (pxp != null) {
				dto.setTotalXp(pxp.getTotalXp());
				dto.setCurrentLevel(pxp.getCurrentLevel());
			} else {
				dto.setTotalXp(0);
				dto.setCurrentLevel(1);
			}

			// Achievements (max 5 most recent titles)
			List<Achievement> achs = achievementRepo.findByPlayerId(p.getId());
			if (achs != null && !achs.isEmpty()) {
				List<String> titles = achs.stream()
						.map(Achievement::getTitle)
						.limit(5)
						.collect(Collectors.toList());
				dto.setAchievements(titles);
			} else {
				dto.setAchievements(new ArrayList<>());
			}

			// Shortlist Count
			long count = shortlistRepo.countByPlayerId(p.getId());
			dto.setShortlistCount(count);

			return dto;
		});
	}

	public void shortlistPlayer(Long recruiterId, Long playerId) {
		Optional<ShortlistedPlayer> existing = shortlistRepo.findByRecruiterIdAndPlayerId(recruiterId, playerId);
		if (existing.isEmpty()) {
			ShortlistedPlayer sp = new ShortlistedPlayer();
			sp.setRecruiterId(recruiterId);
			sp.setPlayerId(playerId);
			shortlistRepo.save(sp);
		}
	}

	// PART 3.1: Remove player from shortlist
	public boolean removeFromShortlist(Long recruiterId, Long playerId) {
		Optional<ShortlistedPlayer> existing = shortlistRepo.findByRecruiterIdAndPlayerId(recruiterId, playerId);
		if (existing.isPresent()) {
			shortlistRepo.delete(existing.get());
			return true;
		}
		return false;
	}

	// PART 3.3: View shortlist with notInvitedOnly filter
	public List<ShortlistedPlayer> getShortlist(Long recruiterId, boolean notInvitedOnly) {
		List<ShortlistedPlayer> shortlist = shortlistRepo.findByRecruiterId(recruiterId);
		if (!notInvitedOnly) {
			return shortlist;
		}
		List<Invite> sentInvites = inviteRepo.findByRecruiterId(recruiterId);
		Set<Long> invitedPlayerIds = sentInvites.stream()
				.map(Invite::getPlayerId)
				.collect(Collectors.toSet());
		return shortlist.stream()
				.filter(sp -> !invitedPlayerIds.contains(sp.getPlayerId()))
				.collect(Collectors.toList());
	}

	public void sendInvite(Long recruiterId, Long playerId) {
		Invite invite = new Invite();
		invite.setRecruiterId(recruiterId);
		invite.setPlayerId(playerId);
		invite.setStatus(InviteStatus.PENDING);
		inviteRepo.save(invite);
	}

	// PART 3.2: Withdraw sent invite (only if status is PENDING)
	public String withdrawInvite(Long inviteId) {
		Optional<Invite> opt = inviteRepo.findById(inviteId);
		if (opt.isEmpty()) {
			return "NOT_FOUND";
		}
		Invite invite = opt.get();
		if (invite.getStatus() != InviteStatus.PENDING) {
			return "CANNOT_WITHDRAW_" + invite.getStatus();
		}
		inviteRepo.delete(invite);
		return "SUCCESS";
	}

	public List<Invite> getSentInvites(Long recruiterId) {
		return inviteRepo.findByRecruiterId(recruiterId);
	}

	// PART 3.4: Get managed team for recruiter
	public Team getManagedTeam(Long userId) {
		List<Team> teams = teamRepo.findByManagerId(userId);
		if (teams == null || teams.isEmpty()) {
			RecruiterProfile rp = recruiterRepo.findByUserId(userId);
			if (rp != null) {
				teams = teamRepo.findByManagerId(rp.getId());
			}
		}
		if (teams == null || teams.isEmpty()) {
			RecruiterProfile rp = recruiterRepo.findById(userId).orElse(null);
			if (rp != null && rp.getUserId() != null) {
				teams = teamRepo.findByManagerId(rp.getUserId());
			}
		}
		return (teams != null && !teams.isEmpty()) ? teams.get(0) : null;
	}

	// PART 3.5: OTP Verification flow for Recruiter
	public boolean sendEmailOtp(Long id) {
		RecruiterProfile r = recruiterRepo.findById(id).orElse(null);
		if (r == null) return false;

		User user = r.getUser();
		if (user == null && r.getUserId() != null) {
			user = userRepo.findById(r.getUserId()).orElse(null);
		}
		if (user == null || user.getEmail() == null) return false;

		String otp = String.valueOf(100000 + new Random().nextInt(900000));
		r.setEmailOtp(otp);
		r.setOtpExpiresAt(Instant.now().plus(10, ChronoUnit.MINUTES));
		recruiterRepo.save(r);

		String subject = "Areanix - Recruiter Email Verification OTP";
		String message = "Hi " + (user.getFullname() != null ? user.getFullname() : "Recruiter") + "\n\n"
				+ "Your OTP to verify your Areanix recruiter account is: " + otp + "\n\n"
				+ "This OTP expires in 10 minutes.\n\n"
				+ "If you did not request this, you can ignore this email.\n\n"
				+ "Areanix Team!!";
		emailService.sendemail(user.getEmail(), subject, message);
		return true;
	}

	public RecruiterProfile verifyEmailOtp(Long id, String otp) {
		RecruiterProfile r = recruiterRepo.findById(id).orElse(null);
		if (r == null) return null;

		if (r.getEmailOtp() != null && r.getEmailOtp().equals(otp)
				&& r.getOtpExpiresAt() != null && r.getOtpExpiresAt().isAfter(Instant.now())) {
			r.setEmailVerified(true);
			r.setEmailOtp(null);
			r.setOtpExpiresAt(null);
			r.setVerificationStatus(VerificationStatus.VERIFIED);
			return recruiterRepo.save(r);
		}
		return null;
	}

	// Player-Facing Helper Methods
	public List<Invite> getPlayerInvites(Long playerId) {
		return inviteRepo.findByPlayerId(playerId);
	}

	public List<ShortlistedPlayer> getPlayerShortlists(Long playerId) {
		return shortlistRepo.findByPlayerId(playerId);
	}

	public long getPlayerShortlistCount(Long playerId) {
		return shortlistRepo.countByPlayerId(playerId);
	}

	public Invite acceptInvite(Long inviteId) {
		Optional<Invite> opt = inviteRepo.findById(inviteId);
		if (opt.isPresent()) {
			Invite inv = opt.get();
			inv.setStatus(InviteStatus.ACCEPTED);
			Invite saved = inviteRepo.save(inv);

			try {
				Long recId = inv.getRecruiterId();
				Long pId = inv.getPlayerId();

				List<Team> teams = teamRepo.findByManagerId(recId);
				if (teams == null || teams.isEmpty()) {
					RecruiterProfile rp = recruiterRepo.findById(recId).orElse(null);
					if (rp != null && rp.getUserId() != null) {
						teams = teamRepo.findByManagerId(rp.getUserId());
					}
				}

				Team targetTeam = null;
				if (teams != null && !teams.isEmpty()) {
					targetTeam = teams.get(0);
				} else {
					RecruiterProfile rp = recruiterRepo.findById(recId).orElse(null);
					String orgName = (rp != null && rp.getOrganizationName() != null) ? rp.getOrganizationName() : "Esports Squad";
					Team newTeam = new Team();
					newTeam.setName(orgName + " Roster");
					newTeam.setGameFocus((rp != null && rp.getGamesRecruiting() != null) ? rp.getGamesRecruiting() : "BGMI");
					newTeam.setRegion((rp != null && rp.getRegion() != null) ? rp.getRegion() : "Asia");
					newTeam.setManagerId(recId);
					targetTeam = teamRepo.save(newTeam);
				}

				if (targetTeam != null) {
					List<TeamMember> existing = teamMemberRepo.findByPlayerIdAndLeftAtIsNull(pId);
					if (existing == null || existing.isEmpty()) {
						TeamMember tm = new TeamMember();
						tm.setTeamId(targetTeam.getId());
						tm.setPlayerId(pId);
						tm.setJoinedAt(Instant.now());
						teamMemberRepo.save(tm);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return saved;
		}
		return null;
	}

	public Invite declineInvite(Long inviteId) {
		Optional<Invite> opt = inviteRepo.findById(inviteId);
		if (opt.isPresent()) {
			Invite inv = opt.get();
			inv.setStatus(InviteStatus.DECLINED);
			return inviteRepo.save(inv);
		}
		return null;
	}

	public List<RecruiterProfile> getAllRecruiters() {
		return recruiterRepo.findAll();
	}

	public Invite applyToRecruiter(Long recruiterId, Long playerId) {
		Invite app = new Invite();
		app.setRecruiterId(recruiterId);
		app.setPlayerId(playerId);
		app.setStatus(InviteStatus.PENDING);
		return inviteRepo.save(app);
	}

	public RecruiterProfile getByUserId(Long userId) {
		return recruiterRepo.findByUserId(userId);
	}

	public RecruiterProfile getById(Long id) {
		return recruiterRepo.findById(id).orElse(null);
	}

	public RecruiterProfile updateProfile(Long id, RecruiterProfile updated) {
		RecruiterProfile r = recruiterRepo.findById(id).orElse(null);
		if (r == null) return null;
		if (updated.getOrganizationName() != null) r.setOrganizationName(updated.getOrganizationName());
		if (updated.getRegion() != null) r.setRegion(updated.getRegion());
		if (updated.getWebsiteUrl() != null) r.setWebsiteUrl(updated.getWebsiteUrl());
		if (updated.getLogoUrl() != null) r.setLogoUrl(updated.getLogoUrl());
		if (updated.getGamesRecruiting() != null) r.setGamesRecruiting(updated.getGamesRecruiting());
		if (updated.getBio() != null) r.setBio(updated.getBio());
		return recruiterRepo.save(r);
	}

}

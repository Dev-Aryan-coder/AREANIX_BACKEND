package com.example.Areanixx.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Areanixx.Entity.Dispute;
import com.example.Areanixx.Entity.Organizer;
import com.example.Areanixx.Entity.PlayerXP;
import com.example.Areanixx.Entity.RecruiterProfile;
import com.example.Areanixx.Entity.ReportStatus;
import com.example.Areanixx.Entity.Tournament;
import com.example.Areanixx.Entity.TournamentReport;
import com.example.Areanixx.Entity.TournamentStatus;
import com.example.Areanixx.Entity.User;
import com.example.Areanixx.Entity.VerificationStatus;
import com.example.Areanixx.Repo.AchievementRepo;
import com.example.Areanixx.Repo.DisputeRepo;
import com.example.Areanixx.Repo.OrganizerRepo;
import com.example.Areanixx.Repo.PlayerXPRepo;
import com.example.Areanixx.Repo.RecruiterProfileRepo;
import com.example.Areanixx.Repo.TournamentReportRepo;
import com.example.Areanixx.Repo.TournamentRepo;
import com.example.Areanixx.Repo.UserRepo;

@Service
public class AdminService {

	@Autowired
	private OrganizerRepo organizerRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private RecruiterProfileRepo recruiterProfileRepo;
	@Autowired
	private TournamentReportRepo reportRepo;
	@Autowired
	private DisputeRepo disputeRepo;
	@Autowired
	private TournamentRepo tournamentRepo;
	@Autowired
	private PlayerXPRepo playerXPRepo;
	@Autowired
	private AchievementRepo achievementRepo;

	// User Management
	public List<User> getAllUsers() {
		return userRepo.findAll();
	}

	public List<User> searchUsers(String query) {
		return userRepo.searchByNameOrTag(query);
	}

	public User suspendUser(Long id) {
		User u = userRepo.findById(id).orElse(null);
		if (u == null) return null;
		u.setActive(false);
		return userRepo.save(u);
	}

	public User unsuspendUser(Long id) {
		User u = userRepo.findById(id).orElse(null);
		if (u == null) return null;
		u.setActive(true);
		return userRepo.save(u);
	}

	// Organizer Moderation
	public List<Organizer> getAllOrganizers() {
		return organizerRepo.findAll();
	}

	public List<Organizer> getPendingOrganizers() {
		return organizerRepo.findByVerificationStatus(VerificationStatus.PENDING);
	}

	public Organizer approveOrganizer(Long id) {
		Organizer o = organizerRepo.findById(id).orElse(null);
		if (o == null) {
			o = organizerRepo.findByUserId(id);
		}
		if (o == null) return null;
		o.setVerificationStatus(VerificationStatus.VERIFIED);
		return organizerRepo.save(o);
	}

	public Organizer rejectOrganizer(Long id, String reason) {
		Organizer o = organizerRepo.findById(id).orElse(null);
		if (o == null) {
			o = organizerRepo.findByUserId(id);
		}
		if (o == null) return null;
		o.setVerificationStatus(VerificationStatus.REJECTED);
		o.setRejectionReason(reason);
		return organizerRepo.save(o);
	}

	public Organizer verifyChannel(Long id, boolean verified) {
		Organizer o = organizerRepo.findById(id).orElse(null);
		if (o == null) {
			o = organizerRepo.findByUserId(id);
		}
		if (o == null) return null;
		o.setChannelVerified(verified);
		return organizerRepo.save(o);
	}

	// Recruiter Moderation
	public List<RecruiterProfile> getAllRecruiters() {
		return recruiterProfileRepo.findAll();
	}

	public List<RecruiterProfile> getPendingRecruiters() {
		return recruiterProfileRepo.findByVerificationStatus(VerificationStatus.PENDING);
	}

	public RecruiterProfile approveRecruiter(Long id) {
		RecruiterProfile r = recruiterProfileRepo.findById(id).orElse(null);
		if (r == null) {
			r = recruiterProfileRepo.findByUserId(id);
		}
		if (r == null) return null;
		r.setVerificationStatus(VerificationStatus.VERIFIED);
		return recruiterProfileRepo.save(r);
	}

	public RecruiterProfile rejectRecruiter(Long id, String reason) {
		RecruiterProfile r = recruiterProfileRepo.findById(id).orElse(null);
		if (r == null) {
			r = recruiterProfileRepo.findByUserId(id);
		}
		if (r == null) return null;
		r.setVerificationStatus(VerificationStatus.REJECTED);
		r.setRejectionReason(reason);
		return recruiterProfileRepo.save(r);
	}

	// Tournament Moderation & Reports
	public List<TournamentReport> getFlaggedTournaments() {
		return reportRepo.findByStatus(ReportStatus.OPEN);
	}

	public TournamentReport reviewReport(Long reportId) {
		TournamentReport r = reportRepo.findById(reportId).orElse(null);
		if (r == null) return null;
		r.setStatus(ReportStatus.REVIEWED);
		return reportRepo.save(r);
	}

	public Tournament cancelTournament(Long tournamentId) {
		Tournament t = tournamentRepo.findById(tournamentId).orElse(null);
		if (t == null) return null;
		t.setStatus(TournamentStatus.CANCELLED);
		return tournamentRepo.save(t);
	}

	// Dispute Resolution
	public List<Dispute> getOpenDisputes() {
		return disputeRepo.findByStatus(ReportStatus.OPEN);
	}

	public Dispute resolveDispute(Long disputeId) {
		Dispute d = disputeRepo.findById(disputeId).orElse(null);
		if (d == null) return null;
		d.setStatus(ReportStatus.REVIEWED);
		return disputeRepo.save(d);
	}

	// Rich Platform-Wide Stats
	public Map<String, Object> getPlatformStats() {
		Map<String, Object> stats = new HashMap<>();
		stats.put("totalUsers", userRepo.count());
		stats.put("pendingOrganizers", (long) organizerRepo.findByVerificationStatus(VerificationStatus.PENDING).size());
		stats.put("openReports", (long) reportRepo.findByStatus(ReportStatus.OPEN).size());
		stats.put("openDisputes", (long) disputeRepo.findByStatus(ReportStatus.OPEN).size());

		stats.put("totalOrganizers", organizerRepo.count());
		stats.put("totalRecruiters", recruiterProfileRepo.count());
		stats.put("totalTournaments", tournamentRepo.count());

		List<Tournament> completedTournaments = tournamentRepo.findByStatus(TournamentStatus.COMPLETED);
		stats.put("completedTournaments", (long) (completedTournaments != null ? completedTournaments.size() : 0));

		double totalPrizePoolAwarded = 0.0;
		if (completedTournaments != null) {
			for (Tournament t : completedTournaments) {
				totalPrizePoolAwarded += t.getPrizePool();
			}
		}
		stats.put("totalPrizePoolAwarded", totalPrizePoolAwarded);

		long totalXpAwardedPlatformWide = 0L;
		List<PlayerXP> allXp = playerXPRepo.findAll();
		if (allXp != null) {
			for (PlayerXP xp : allXp) {
				totalXpAwardedPlatformWide += xp.getTotalXp();
			}
		}
		stats.put("totalXpAwardedPlatformWide", totalXpAwardedPlatformWide);

		stats.put("totalAchievementsGenerated", achievementRepo.count());

		Instant oneWeekAgo = Instant.now().minus(7, ChronoUnit.DAYS);
		stats.put("newUsersThisWeek", userRepo.countByCreatedAtAfter(oneWeekAgo));

		return stats;
	}
}

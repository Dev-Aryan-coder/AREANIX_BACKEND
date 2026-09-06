package com.example.Areanixx.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Areanixx.Entity.Dispute;
import com.example.Areanixx.Entity.Organizer;
import com.example.Areanixx.Entity.RegistrationStatus;
import com.example.Areanixx.Entity.ReportStatus;
import com.example.Areanixx.Entity.Tournament;
import com.example.Areanixx.Entity.TournamentRegistration;
import com.example.Areanixx.Entity.TournamentStatus;
import com.example.Areanixx.Entity.User;
import com.example.Areanixx.Entity.VerificationStatus;
import com.example.Areanixx.Repo.DisputeRepo;
import com.example.Areanixx.Repo.OrganizerRepo;
import com.example.Areanixx.Repo.TournamentRegistrationRepo;
import com.example.Areanixx.Repo.TournamentRepo;
import com.example.Areanixx.Repo.UserRepo;
import com.example.Areanixx.dto.OrganizerDashboardDto;

@Service
public class OrganizerService {

	@Autowired
	private OrganizerRepo organizerRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private TournamentRepo tournamentRepo;

	@Autowired
	private TournamentRegistrationRepo registrationRepo;

	@Autowired
	private DisputeRepo disputeRepo;

	@Autowired
	private EmailService es;

	public void createOrganizer(Organizer o) {
		Organizer existing = null;
		if (o.getUserId() != null) {
			existing = organizerRepo.findByUserId(o.getUserId());
		}
		if (existing != null) {
			if (o.getOrganizationName() != null) existing.setOrganizationName(o.getOrganizationName());
			if (o.getPhoneNumber() != null) existing.setPhoneNumber(o.getPhoneNumber());
			if (o.getYoutubeChannelUrl() != null) existing.setYoutubeChannelUrl(o.getYoutubeChannelUrl());
			if (o.getLogoUrl() != null) existing.setLogoUrl(o.getLogoUrl());
			if (o.getBannerUrl() != null) existing.setBannerUrl(o.getBannerUrl());
			if (o.getBio() != null) existing.setBio(o.getBio());
			if (o.getSubscriberCount() != null) existing.setSubscriberCount(o.getSubscriberCount());
			if (o.getDiscordUrl() != null) existing.setDiscordUrl(o.getDiscordUrl());
			if (o.getTwitterUrl() != null) existing.setTwitterUrl(o.getTwitterUrl());
			if (o.getTwitchUrl() != null) existing.setTwitchUrl(o.getTwitchUrl());
			organizerRepo.save(existing);
		} else {
			if (o.getVerificationStatus() == null) {
				o.setVerificationStatus(VerificationStatus.PENDING);
			}
			organizerRepo.save(o);
		}
	}

	// step 1: generates a real random 6-digit OTP, valid 10 minutes,
	// emails it to the organizer's account email (via linked User)
	public void sendEmailOtp(Long id) {
		Organizer o = organizerRepo.findById(id).orElse(null);
		if (o == null) {
			o = organizerRepo.findByUserId(id);
		}
		
		// If organizer record does not exist yet for this user, auto-provision one
		if (o == null) {
			User user = userRepo.findById(id).orElse(null);
			if (user != null) {
				o = new Organizer();
				o.setUserId(user.getId());
				o.setOrganizationName(user.getFullname() != null ? user.getFullname() : "Organizer " + user.getId());
				o.setPhoneNumber("987" + String.format("%07d", user.getId()));
				o.setYoutubeChannelUrl("https://youtube.com/@areanix");
				o.setVerificationStatus(VerificationStatus.PENDING);
				o = organizerRepo.save(o);
			}
		}

		if (o == null) return;

		User u = o.getUser();
		if (u == null && o.getUserId() != null) {
			u = userRepo.findById(o.getUserId()).orElse(null);
		}
		if (u == null || u.getEmail() == null) return;

		String otp = String.valueOf(100000 + new java.util.Random().nextInt(900000));
		o.setEmailOtp(otp);
		o.setOtpExpiresAt(Instant.now().plus(10, ChronoUnit.MINUTES));
		organizerRepo.save(o);

		try {
			String subject = "Areanix - Organizer Email Verification OTP";
			String message = "Hi " + u.getFullname() + "\n\n"
					+ "Your OTP to verify your Areanix organizer account is: " + otp + "\n\n"
					+ "This OTP expires in 10 minutes.\n\n"
					+ "If you did not request this, you can ignore this email.\n\n"
					+ "Areanix Team!!";
			es.sendemail(u.getEmail(), subject, message);
		} catch (Exception ex) {
			System.out.println("Email send failed: " + ex.getMessage());
		}
	}

	// step 2: user submits the OTP they received by email
	public Organizer verifyEmailOtp(Long id, String otp) {
		Organizer o = organizerRepo.findById(id).orElse(null);
		if (o == null) {
			o = organizerRepo.findByUserId(id);
		}
		if (o == null) return null;

		if (o.getEmailOtp() != null && o.getEmailOtp().equals(otp)
				&& o.getOtpExpiresAt() != null && o.getOtpExpiresAt().isAfter(Instant.now())) {
			o.setEmailVerified(true);
			o.setEmailOtp(null); // invalidate immediately so it can't be replayed
			o.setOtpExpiresAt(null);
			if (Boolean.TRUE.equals(o.getEmailVerified())) {
				o.setVerificationStatus(VerificationStatus.VERIFIED);
			}
		}
		return organizerRepo.save(o);
	}

	public VerificationStatus getVerificationStatus(Long id) {
		Organizer o = organizerRepo.findById(id).orElse(null);
		if (o == null) {
			o = organizerRepo.findByUserId(id);
		}
		return (o != null) ? o.getVerificationStatus() : null;
	}

	public Organizer getbyid(Long id) {
		Organizer o = organizerRepo.findById(id).orElse(null);
		if (o != null) return o;
		return organizerRepo.findByUserId(id);
	}

	public Organizer updateProfile(Long id, Organizer updates) {
		Organizer o = organizerRepo.findById(id).orElse(null);
		if (o == null) {
			o = organizerRepo.findByUserId(id);
		}
		if (o == null) return null;

		if (updates.getOrganizationName() != null) o.setOrganizationName(updates.getOrganizationName());
		if (updates.getLogoUrl() != null) o.setLogoUrl(updates.getLogoUrl());
		if (updates.getBannerUrl() != null) o.setBannerUrl(updates.getBannerUrl());
		if (updates.getBio() != null) o.setBio(updates.getBio());
		if (updates.getSubscriberCount() != null) o.setSubscriberCount(updates.getSubscriberCount());
		if (updates.getDiscordUrl() != null) o.setDiscordUrl(updates.getDiscordUrl());
		if (updates.getTwitterUrl() != null) o.setTwitterUrl(updates.getTwitterUrl());
		if (updates.getTwitchUrl() != null) o.setTwitchUrl(updates.getTwitchUrl());
		if (updates.getPhoneNumber() != null) o.setPhoneNumber(updates.getPhoneNumber());
		if (updates.getYoutubeChannelUrl() != null) o.setYoutubeChannelUrl(updates.getYoutubeChannelUrl());

		return organizerRepo.save(o);
	}

	public OrganizerDashboardDto getDashboard(Long organizerId) {
		Organizer o = organizerRepo.findById(organizerId).orElse(null);
		if (o == null) {
			o = organizerRepo.findByUserId(organizerId);
		}
		if (o == null) return null;

		Long realOrgId = o.getId();
		List<Tournament> tournaments = tournamentRepo.findByOrganizerId(realOrgId);

		OrganizerDashboardDto dto = new OrganizerDashboardDto();
		dto.setOrganizerId(realOrgId);
		dto.setOrganizationName(o.getOrganizationName());
		dto.setVerificationStatus(o.getVerificationStatus());
		dto.setTotalTournamentsHosted(tournaments.size());

		Map<String, Integer> byStatus = new HashMap<>();
		byStatus.put("UPCOMING", 0);
		byStatus.put("ONGOING", 0);
		byStatus.put("COMPLETED", 0);

		double totalPrizePoolAwarded = 0.0;
		for (Tournament t : tournaments) {
			if (t.getStatus() != null) {
				String statusKey = t.getStatus().name();
				byStatus.put(statusKey, byStatus.getOrDefault(statusKey, 0) + 1);
				if (t.getStatus() == TournamentStatus.COMPLETED) {
					totalPrizePoolAwarded += t.getPrizePool();
				}
			}
		}
		dto.setTournamentsByStatus(byStatus);
		dto.setTotalPrizePoolAwarded(totalPrizePoolAwarded);

		List<Long> tournamentIds = tournaments.stream().map(Tournament::getId).toList();
		int totalPlayersReached = 0;
		int openDisputeCount = 0;

		if (!tournamentIds.isEmpty()) {
			List<TournamentRegistration> approvedRegs = registrationRepo.findByTournamentIdInAndStatus(tournamentIds, RegistrationStatus.APPROVED);
			totalPlayersReached = approvedRegs.size();

			List<Dispute> openDisputes = disputeRepo.findByTournamentIdInAndStatus(tournamentIds, ReportStatus.OPEN);
			openDisputeCount = openDisputes.size();
		}

		dto.setTotalPlayersReached(totalPlayersReached);
		dto.setOpenDisputeCount(openDisputeCount);

		List<Tournament> recent = tournaments.stream()
				.sorted((a, b) -> Long.compare(b.getId() != null ? b.getId() : 0, a.getId() != null ? a.getId() : 0))
				.limit(5)
				.toList();
		dto.setRecentTournaments(recent);

		return dto;
	}

	public List<Dispute> getDisputesForOrganizer(Long organizerId) {
		Organizer o = organizerRepo.findById(organizerId).orElse(null);
		if (o == null) {
			o = organizerRepo.findByUserId(organizerId);
		}
		if (o == null) return Collections.emptyList();

		List<Tournament> tournaments = tournamentRepo.findByOrganizerId(o.getId());
		if (tournaments.isEmpty()) return Collections.emptyList();

		List<Long> tournamentIds = tournaments.stream().map(Tournament::getId).toList();
		return disputeRepo.findByTournamentIdIn(tournamentIds);
	}

	public List<TournamentRegistration> getPendingRegistrations(Long organizerId) {
		Organizer o = organizerRepo.findById(organizerId).orElse(null);
		if (o == null) {
			o = organizerRepo.findByUserId(organizerId);
		}
		if (o == null) return Collections.emptyList();

		List<Tournament> tournaments = tournamentRepo.findByOrganizerId(o.getId());
		if (tournaments.isEmpty()) return Collections.emptyList();

		List<Long> tournamentIds = tournaments.stream().map(Tournament::getId).toList();
		return registrationRepo.findByTournamentIdInAndStatus(tournamentIds, RegistrationStatus.PENDING);
	}
}

package com.example.Areanixx.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Areanixx.Entity.Dispute;
import com.example.Areanixx.Entity.Match;
import com.example.Areanixx.Entity.PlayerXP;
import com.example.Areanixx.Entity.RegistrationStatus;
import com.example.Areanixx.Entity.Tournament;
import com.example.Areanixx.Entity.TournamentRegistration;
import com.example.Areanixx.Entity.TournamentReport;
import com.example.Areanixx.Entity.TournamentResult;
import com.example.Areanixx.Entity.TournamentStatus;
import com.example.Areanixx.Repo.AchievementRepo;
import com.example.Areanixx.Repo.DisputeRepo;
import com.example.Areanixx.Repo.MatchRepo;
import com.example.Areanixx.Repo.PlayerXPRepo;
import com.example.Areanixx.Repo.TournamentRegistrationRepo;
import com.example.Areanixx.Repo.TournamentRepo;
import com.example.Areanixx.Repo.TournamentReportRepo;
import com.example.Areanixx.Repo.TournamentResultRepo;
import com.example.Areanixx.Repo.XPTransactionRepo;

@Service
public class TournamentService {

	@Autowired
	private TournamentRepo tournamentRepo;
	@Autowired
	private TournamentRegistrationRepo registrationRepo;
	@Autowired
	private MatchRepo matchRepo;
	@Autowired
	private TournamentResultRepo resultRepo;
	@Autowired
	private PlayerXPRepo xpRepo;
	@Autowired
	private XPTransactionRepo xpTxRepo;
	@Autowired
	private AchievementRepo achievementRepo;
	@Autowired
	private TournamentReportRepo reportRepo;
	@Autowired
	private DisputeRepo disputeRepo;

	public List<Tournament> getTournamentsByStatus(TournamentStatus status) {
		return tournamentRepo.findByStatus(status);
	}

	public void createTournament(Tournament t) {
		t.setStatus(TournamentStatus.UPCOMING);
		tournamentRepo.save(t);
	}

	public Tournament getTournamentDetail(Long id) {
		return tournamentRepo.findById(id).orElse(null);
	}

	public Tournament startTournament(Long id) {
		Tournament t = tournamentRepo.findById(id).orElse(null);
		if (t == null) return null;
		t.setStatus(TournamentStatus.ONGOING);
		return tournamentRepo.save(t);
	}

	public Tournament releaseRoomDetails(Long id, String roomId, String roomPassword) {
		Tournament t = tournamentRepo.findById(id).orElse(null);
		if (t == null) return null;
		t.setRoomId(roomId);
		t.setRoomPassword(roomPassword);
		t.setStatus(TournamentStatus.ONGOING);
		return tournamentRepo.save(t);
	}

	public TournamentRegistration registerForTournament(Long tournamentId, Long playerId, Long teamId) {
		// Prevent duplicate registration for the same tournament & team
		if (teamId != null) {
			List<TournamentRegistration> existing = registrationRepo.findByTournamentIdAndTeamId(tournamentId, teamId);
			if (existing != null && !existing.isEmpty()) {
				return existing.get(0);
			}
		} else if (playerId != null) {
			List<TournamentRegistration> existing = registrationRepo.findByTournamentIdAndPlayerId(tournamentId, playerId);
			if (existing != null && !existing.isEmpty()) {
				return existing.get(0);
			}
		}

		TournamentRegistration reg = new TournamentRegistration();
		reg.setTournamentId(tournamentId);
		reg.setPlayerId(playerId);
		reg.setTeamId(teamId);
		reg.setStatus(RegistrationStatus.PENDING);
		return registrationRepo.save(reg);
	}

	public List<TournamentRegistration> getTeamRegistrations(Long teamId) {
		return registrationRepo.findByTeamId(teamId);
	}

	public TournamentRegistration approveRegistration(Long regId) {
		TournamentRegistration reg = registrationRepo.findById(regId).orElse(null);
		if (reg == null) return null;
		reg.setStatus(RegistrationStatus.APPROVED);
		return registrationRepo.save(reg);
	}

	public TournamentRegistration rejectRegistration(Long regId) {
		TournamentRegistration reg = registrationRepo.findById(regId).orElse(null);
		if (reg == null) return null;
		reg.setStatus(RegistrationStatus.REJECTED);
		return registrationRepo.save(reg);
	}

	public Match scheduleMatch(Long tournamentId, int roundNumber, Long teamAId, Long teamBId, java.time.Instant scheduledTime) {
		Match m = new Match();
		m.setTournamentId(tournamentId);
		m.setRound(roundNumber);
		m.setTeamAId(teamAId);
		m.setTeamBId(teamBId);
		m.setScheduledTime(scheduledTime);
		return matchRepo.save(m);
	}

	public TournamentResult enterResult(Long tournamentId, Long playerId, Long teamId, int placement) {
		TournamentResult r = new TournamentResult();
		r.setTournamentId(tournamentId);
		r.setPlayerId(playerId);
		r.setTeamId(teamId);
		r.setPlacement(placement);
		return resultRepo.save(r);
	}

	public Map<String, Object> completeTournament(Long tournamentId) {
		Tournament t = tournamentRepo.findById(tournamentId).orElse(null);
		if (t == null) return null;

		t.setStatus(TournamentStatus.COMPLETED);
		tournamentRepo.save(t);

		List<TournamentResult> results = resultRepo.findByTournamentIdOrderByPlacementAsc(tournamentId);

		int winnerXpAmount = 500;
		int participantXpAmount = 100;
		int totalXpAwarded = 0;

		for (TournamentResult r : results) {
			Long pId = r.getPlayerId();
			if (pId == null) continue;

			int xpToAdd = (r.getPlacement() == 1) ? winnerXpAmount : participantXpAmount;
			totalXpAwarded += xpToAdd;

			PlayerXP pxp = xpRepo.findByPlayerId(pId);
			if (pxp == null) {
				pxp = new PlayerXP();
				pxp.setPlayerId(pId);
				pxp.setTotalXp(0);
			}
			pxp.setTotalXp(pxp.getTotalXp() + xpToAdd);
			xpRepo.save(pxp);

			com.example.Areanixx.Entity.XPTransaction tx = new com.example.Areanixx.Entity.XPTransaction();
			tx.setPlayerId(pId);
			tx.setAmount(xpToAdd);
			tx.setSource(r.getPlacement() == 1 ? com.example.Areanixx.Entity.XPSource.PLACEMENT : com.example.Areanixx.Entity.XPSource.TOURNAMENT_PLAY);
			tx.setReferenceId(tournamentId);
			xpTxRepo.save(tx);

			if (r.getPlacement() == 1) {
				com.example.Areanixx.Entity.Achievement ach = new com.example.Areanixx.Entity.Achievement();
				ach.setPlayerId(pId);
				ach.setTitle("Tournament Champion");
				ach.setTournamentId(tournamentId);
				achievementRepo.save(ach);
			}
		}

		Map<String, Object> summary = new HashMap<>();
		summary.put("tournamentId", tournamentId);
		summary.put("status", "COMPLETED");
		summary.put("participantsAwarded", results.size());
		summary.put("totalXpAwarded", totalXpAwarded);
		return summary;
	}

	public List<TournamentResult> getLiveLeaderboard(Long tournamentId) {
		return resultRepo.findByTournamentIdOrderByPlacementAsc(tournamentId);
	}

	public TournamentReport reportTournament(Long tournamentId, Long userId, String reason) {
		TournamentReport r = new TournamentReport();
		r.setTournamentId(tournamentId);
		r.setReportedBy(userId);
		r.setReason(reason);
		return reportRepo.save(r);
	}

	public Dispute raiseDispute(Long tournamentId, Long userId, String description) {
		Dispute d = new Dispute();
		d.setTournamentId(tournamentId);
		d.setRaisedBy(userId);
		d.setDescription(description);
		return disputeRepo.save(d);
	}
}

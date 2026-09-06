package com.example.Areanixx.Service;

import java.time.Instant;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Areanixx.Entity.Organizer;
import com.example.Areanixx.Entity.PlayerProfile;
import com.example.Areanixx.Entity.RecruiterProfile;
import com.example.Areanixx.Entity.Team;
import com.example.Areanixx.Entity.TeamMember;
import com.example.Areanixx.Entity.Tournament;
import com.example.Areanixx.Entity.TournamentRegistration;
import com.example.Areanixx.Entity.User;
import com.example.Areanixx.Repo.OrganizerRepo;
import com.example.Areanixx.Repo.PlayerProfileRepo;
import com.example.Areanixx.Repo.RecruiterProfileRepo;
import com.example.Areanixx.Repo.TeamMemberRepo;
import com.example.Areanixx.Repo.TeamRepo;
import com.example.Areanixx.Repo.TournamentRegistrationRepo;
import com.example.Areanixx.Repo.TournamentRepo;
import com.example.Areanixx.Repo.UserRepo;

@Service
public class TeamService {

	@Autowired
	private TeamRepo teamRepo;

	@Autowired
	private TeamMemberRepo teamMemberRepo;

	@Autowired
	private RecruiterProfileRepo recruiterRepo;

	@Autowired
	private PlayerProfileRepo playerProfileRepo;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private TournamentRegistrationRepo registrationRepo;

	@Autowired
	private TournamentRepo tournamentRepo;

	@Autowired
	private OrganizerRepo organizerRepo;

	public void createTeam(Team t) {
		teamRepo.save(t);
	}

	public void requestCoManager(Long teamId, Long userId) {
		Team team = teamRepo.findById(teamId).orElse(null);
		if (team != null) {
			team.setCoManagerId(userId);
			teamRepo.save(team);
		}
	}

	public void addMember(Long teamId, Long playerId) {
		TeamMember tm = new TeamMember();
		tm.setTeamId(teamId);
		tm.setPlayerId(playerId);
		tm.setJoinedAt(Instant.now());
		teamMemberRepo.save(tm);
	}

	public void removeMember(Long teamId, Long playerId) {
		TeamMember tm = teamMemberRepo.findByTeamIdAndPlayerIdAndLeftAtIsNull(teamId, playerId);
		if (tm != null) {
			tm.setLeftAt(Instant.now());
			teamMemberRepo.save(tm);
		}
	}

	public List<TeamMember> getRoster(Long teamId) {
		List<TeamMember> roster = teamMemberRepo.findByTeamIdAndLeftAtIsNull(teamId);
		if (roster != null) {
			for (TeamMember tm : roster) {
				if (tm.getPlayer() == null && tm.getPlayerId() != null) {
					PlayerProfile pp = playerProfileRepo.findById(tm.getPlayerId()).orElse(null);
					if (pp == null) {
						pp = playerProfileRepo.findByUserId(tm.getPlayerId());
					}
					if (pp != null) {
						tm.setPlayer(pp);
					}
				}
			}
		}
		return (roster != null) ? roster : Collections.emptyList();
	}

	public List<Team> getManagedTeams(Long managerId) {
		List<Team> teams = teamRepo.findByManagerId(managerId);
		if (teams == null || teams.isEmpty()) {
			RecruiterProfile rp = recruiterRepo.findByUserId(managerId);
			if (rp != null) {
				teams = teamRepo.findByManagerId(rp.getId());
			}
		}
		if (teams == null || teams.isEmpty()) {
			RecruiterProfile rp = recruiterRepo.findById(managerId).orElse(null);
			if (rp != null && rp.getUserId() != null) {
				teams = teamRepo.findByManagerId(rp.getUserId());
			}
		}
		return (teams != null) ? teams : Collections.emptyList();
	}

	public List<Team> searchByName(String query) {
		return teamRepo.findByNameContainingIgnoreCase(query);
	}

	public Map<String, Object> getPlayerTeamDetails(Long playerId) {
		Map<String, Object> res = new HashMap<>();
		List<TeamMember> memberships = teamMemberRepo.findByPlayerIdAndLeftAtIsNull(playerId);
		
		// Fallback: If not found by given ID, try resolving User ID <-> PlayerProfile ID
		if (memberships == null || memberships.isEmpty()) {
			PlayerProfile pp = playerProfileRepo.findByUserId(playerId);
			if (pp != null) {
				memberships = teamMemberRepo.findByPlayerIdAndLeftAtIsNull(pp.getId());
			}
		}
		if (memberships == null || memberships.isEmpty()) {
			PlayerProfile pp = playerProfileRepo.findById(playerId).orElse(null);
			if (pp != null && pp.getUserId() != null) {
				memberships = teamMemberRepo.findByPlayerIdAndLeftAtIsNull(pp.getUserId());
			}
		}

		if (memberships == null || memberships.isEmpty()) {
			res.put("hasTeam", false);
			res.put("team", null);
			res.put("membership", null);
			res.put("roster", Collections.emptyList());
			res.put("teamTournaments", Collections.emptyList());
			return res;
		}

		TeamMember activeMember = memberships.get(0);
		Team team = teamRepo.findById(activeMember.getTeamId()).orElse(null);
		List<TeamMember> roster = getRoster(activeMember.getTeamId());

		// Dynamically resolve manager info
		if (team != null && team.getManagerId() != null) {
			RecruiterProfile rp = recruiterRepo.findById(team.getManagerId()).orElse(null);
			if (rp == null) {
				rp = recruiterRepo.findByUserId(team.getManagerId());
			}
			if (rp != null) {
				if (rp.getUser() != null) {
					res.put("managerName", rp.getUser().getFullname());
				} else if (rp.getUserId() != null) {
					User u = userRepo.findById(rp.getUserId()).orElse(null);
					if (u != null) {
						res.put("managerName", u.getFullname());
					}
				}
				if (!res.containsKey("managerName") || res.get("managerName") == null) {
					res.put("managerName", rp.getOrganizationName());
				}
				res.put("organizationName", rp.getOrganizationName());
			} else {
				User u = userRepo.findById(team.getManagerId()).orElse(null);
				if (u != null) {
					res.put("managerName", u.getFullname());
				}
			}
		}

		// Dynamically resolve squad tournament registrations
		List<Map<String, Object>> teamTournaments = new ArrayList<>();
		try {
			List<TournamentRegistration> teamRegs = registrationRepo.findByTeamId(activeMember.getTeamId());
			if (teamRegs != null) {
				for (TournamentRegistration tr : teamRegs) {
					Map<String, Object> tMap = new HashMap<>();
					tMap.put("registrationId", tr.getId());
					tMap.put("tournamentId", tr.getTournamentId());
					tMap.put("status", tr.getStatus() != null ? tr.getStatus().name() : "PENDING");
					
					Tournament t = tournamentRepo.findById(tr.getTournamentId()).orElse(null);
					if (t != null) {
						tMap.put("tournamentName", t.getName());
						tMap.put("game", t.getGame() != null ? t.getGame() : "BGMI");
						tMap.put("region", t.getRegion() != null ? t.getRegion() : "Asia");
						tMap.put("prizePool", t.getPrizePool());
						tMap.put("tournamentStatus", t.getStatus() != null ? t.getStatus().name() : "UPCOMING");
						tMap.put("startDate", t.getRegistrationOpenAt() != null ? t.getRegistrationOpenAt().toString() : null);
						tMap.put("roomId", t.getRoomId() != null ? t.getRoomId() : "Not Released Yet");
						tMap.put("roomPassword", t.getRoomPassword() != null ? t.getRoomPassword() : "Not Released Yet");
						if (t.getOrganizer() != null && t.getOrganizer().getUser() != null) {
							tMap.put("hostName", t.getOrganizer().getUser().getFullname());
						} else if (t.getOrganizerId() != null) {
							Organizer org = organizerRepo.findById(t.getOrganizerId()).orElse(null);
							if (org != null && org.getUser() != null) {
								tMap.put("hostName", org.getUser().getFullname());
							} else {
								tMap.put("hostName", "Verified Host");
							}
						} else {
							tMap.put("hostName", "Verified Host");
						}
					}
					teamTournaments.add(tMap);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		res.put("hasTeam", true);
		res.put("team", team);
		res.put("membership", activeMember);
		res.put("roster", roster);
		res.put("teamTournaments", teamTournaments);
		return res;
	}
}

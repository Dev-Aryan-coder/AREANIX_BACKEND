package com.example.Areanixx.dto;

import java.util.List;
import java.util.Map;
import com.example.Areanixx.Entity.Tournament;
import com.example.Areanixx.Entity.VerificationStatus;

public class OrganizerDashboardDto {

	public Long getOrganizerId() { return organizerId; }
	public void setOrganizerId(Long organizerId) { this.organizerId = organizerId; }
	public String getOrganizationName() { return organizationName; }
	public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
	public VerificationStatus getVerificationStatus() { return verificationStatus; }
	public void setVerificationStatus(VerificationStatus verificationStatus) { this.verificationStatus = verificationStatus; }
	public int getTotalTournamentsHosted() { return totalTournamentsHosted; }
	public void setTotalTournamentsHosted(int totalTournamentsHosted) { this.totalTournamentsHosted = totalTournamentsHosted; }
	public Map<String, Integer> getTournamentsByStatus() { return tournamentsByStatus; }
	public void setTournamentsByStatus(Map<String, Integer> tournamentsByStatus) { this.tournamentsByStatus = tournamentsByStatus; }
	public int getTotalPlayersReached() { return totalPlayersReached; }
	public void setTotalPlayersReached(int totalPlayersReached) { this.totalPlayersReached = totalPlayersReached; }
	public double getTotalPrizePoolAwarded() { return totalPrizePoolAwarded; }
	public void setTotalPrizePoolAwarded(double totalPrizePoolAwarded) { this.totalPrizePoolAwarded = totalPrizePoolAwarded; }
	public List<Tournament> getRecentTournaments() { return recentTournaments; }
	public void setRecentTournaments(List<Tournament> recentTournaments) { this.recentTournaments = recentTournaments; }
	public int getOpenDisputeCount() { return openDisputeCount; }
	public void setOpenDisputeCount(int openDisputeCount) { this.openDisputeCount = openDisputeCount; }

	private Long organizerId;
	private String organizationName;
	private VerificationStatus verificationStatus;
	private int totalTournamentsHosted;
	private Map<String, Integer> tournamentsByStatus;
	private int totalPlayersReached;
	private double totalPrizePoolAwarded;
	private List<Tournament> recentTournaments;
	private int openDisputeCount;

	public OrganizerDashboardDto() {
	}
}

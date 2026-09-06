# Areanix Esports Platform - Backend API

Enterprise Spring Boot REST API for the **Areanix Esports Platform**, powering:
- **Player Ecosystem:** Profiles, Live Stats, XP & Gamification Leveling, Leaderboards, Social Layer (Friend Requests & Activity)
- **Recruitment Module:** Recruiter Profiles, Talent Scouting DTO Filters, Shortlisting, Official Contract Invites, OTP Email Verification
- **Teams & Roster Engine:** Squad Creation, Manager & Co-Manager Assignment, Roster Roster Enrolment
- **Tournament Management:** Brackets, Match Scheduling, Automated Placement XP & Achievements, Custom Lobby Room Credentials Release, Dispute Resolution & Flagging System
- **Admin Moderation:** Organizer & Recruiter Verification, User Suspensions, Platform Analytics

---

## 🛠️ Tech Stack
- **Framework:** Spring Boot 3.x
- **Language:** Java 17+
- **Persistence:** Spring Data JPA, Hibernate, MySQL 8.0
- **Build Tool:** Maven

---

## 🚀 Setup & Execution
1. Clone the repository:
   ```bash
   git clone https://github.com/Dev-Aryan-coder/AREANIX_BACKEND.git
   ```
2. Copy configuration template:
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```
3. Update MySQL and Email credentials in `src/main/resources/application.properties`.
4. Run locally:
   ```bash
   mvn spring-boot:run
   ```

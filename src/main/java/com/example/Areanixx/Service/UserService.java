package com.example.Areanixx.Service;

import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.Areanixx.Entity.PasswordResetOtp;
import com.example.Areanixx.Entity.Role;
import com.example.Areanixx.Entity.User;
import com.example.Areanixx.Entity.UserRole;
import com.example.Areanixx.Repo.PasswordResetOtpRepo;
import com.example.Areanixx.Repo.UserRepo;
import com.example.Areanixx.Repo.UserRoleRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private UserRoleRepo userRoleRepo;
	@Autowired
	private PasswordResetOtpRepo otpRepo;
	@Autowired
	private EmailService es;

	// Pure registration: creates the base user row
	public void registerUser(User user) {
		userRepo.save(user);

		try {
			String subject = "Welcome to Areanix!!";
			String message = "Hi " + user.getFullname() + "\n\n"
					+ "Welcome to Areanix - the LinkedIn for esports.\n"
					+ "Thank you for registering, we are excited to have you with us.\n\n"
					+ "Warm Regards\n"
					+ "Areanix Team!!";
			es.sendemail(user.getEmail(), subject, message);
			System.out.println("mail sent to " + user.getEmail());
		} catch (Exception e) {
			System.out.println("Mail sending notification skipped: " + e.getMessage());
		}
	}

	public User login(String email, String rawPassword) {
		User user = userRepo.findByEmail(email);
		if (user == null || user.getPassword() == null) return null;
		return user.getPassword().equals(rawPassword) ? user : null;
	}

	public void forgotPassword(String email) {
		User user = userRepo.findByEmail(email);
		if (user == null) return;

		String otp = String.valueOf(100000 + new java.util.Random().nextInt(900000));

		PasswordResetOtp resetOtp = new PasswordResetOtp();
		resetOtp.setUserId(user.getId());
		resetOtp.setOtp(otp);
		resetOtp.setExpiresAt(Instant.now().plus(10, java.time.temporal.ChronoUnit.MINUTES));
		resetOtp.setVerified(false);
		otpRepo.save(resetOtp);

		try {
			String subject = "Areanix - Password Reset OTP";
			String message = "Hi " + user.getFullname() + "\n\n"
					+ "Your OTP to reset your Areanix password is: " + otp + "\n\n"
					+ "This OTP expires in 10 minutes.\n\n"
					+ "If you did not request this, you can ignore this email.\n\n"
					+ "Areanix Team!!";
			es.sendemail(user.getEmail(), subject, message);
		} catch (Exception e) {
			System.out.println("OTP email skipped: " + e.getMessage());
		}
	}

	public boolean verifyOtp(String email, String otp) {
		User user = userRepo.findByEmail(email);
		if (user == null) return false;

		PasswordResetOtp resetOtp = otpRepo.findByUserIdAndOtp(user.getId(), otp);
		if (resetOtp == null || resetOtp.getExpiresAt().isBefore(Instant.now())) {
			return false;
		}
		resetOtp.setVerified(true);
		otpRepo.save(resetOtp);
		return true;
	}

	public boolean resetPassword(String email, String otp, String newPassword) {
		User user = userRepo.findByEmail(email);
		if (user == null) return false;

		PasswordResetOtp resetOtp = otpRepo.findByUserIdAndOtp(user.getId(), otp);
		if (resetOtp == null || !resetOtp.getVerified() || resetOtp.getExpiresAt().isBefore(Instant.now())) {
			return false;
		}

		user.setPassword(newPassword);
		userRepo.save(user);
		return true;
	}

	// Set primary role cleanly on onboarding (overwriting any draft role)
	public void setPrimaryRole(Long userId, Role role) {
		User u = userRepo.findById(userId).orElse(null);
		if (u != null) {
			u.setRole(role);
			userRepo.save(u);
		}

		List<UserRole> existingRoles = userRoleRepo.findByUserId(userId);
		if (existingRoles != null && !existingRoles.isEmpty()) {
			userRoleRepo.deleteAll(existingRoles);
		}

		UserRole ur = new UserRole();
		ur.setUserId(userId);
		ur.setRole(role);
		userRoleRepo.save(ur);
	}

	// Add an additional secondary role without removing existing
	public void addRole(Long userId, Role role) {
		User u = userRepo.findById(userId).orElse(null);
		if (u != null && u.getRole() == null) {
			u.setRole(role);
			userRepo.save(u);
		}

		List<UserRole> existingRoles = userRoleRepo.findByUserId(userId);
		for (UserRole ur : existingRoles) {
			if (ur.getRole() == role) return; // already holds this role
		}
		UserRole ur = new UserRole();
		ur.setUserId(userId);
		ur.setRole(role);
		userRoleRepo.save(ur);
	}

	public List<UserRole> getRoles(Long userId) {
		return userRoleRepo.findByUserId(userId);
	}

	public List<User> searchByNameOrTag(String query) {
		return userRepo.searchByNameOrTag(query);
	}

	public User getbyid(Long id) {
		return userRepo.findById(id).orElse(null);
	}

	public User patchuser(long id, User usr) {
		User existing = userRepo.findById(id).orElse(null);
		if (existing == null) return null;
		if (usr.getFullname() != null) existing.setFullname(usr.getFullname());
		if (usr.getEmail() != null) existing.setEmail(usr.getEmail());
		if (usr.getRole() != null) {
			existing.setRole(usr.getRole());
			setPrimaryRole(id, usr.getRole());
		}
		if (usr.getActive() != null) existing.setActive(usr.getActive());
		return userRepo.save(existing);
	}
}

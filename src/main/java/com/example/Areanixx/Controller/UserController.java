package com.example.Areanixx.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.Areanixx.Entity.Role;
import com.example.Areanixx.Entity.User;
import com.example.Areanixx.Service.UserService;

@RequestMapping("/user")
@RestController
public class UserController {

	@Autowired
	private UserService us;

	// registration: login already authenticates identity, this creates the base User row
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody User user) {
		us.registerUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body("account created successfully!!!");
	}

	// email/password login 
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
		User user = us.login(email, password);
		if (user != null) return ResponseEntity.ok(user);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid email or password!!");
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<String> forgotPassword(@RequestParam String email) {
		us.forgotPassword(email);
		return ResponseEntity.ok("if an account exists for this email, an OTP has been sent!!!");
	}

	@PostMapping("/verify-otp")
	public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
		boolean valid = us.verifyOtp(email, otp);
		if (valid) return ResponseEntity.ok("otp verified successfully!!!");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("otp is invalid or expired!!");
	}

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String otp, @RequestParam String newPassword) {
		boolean success = us.resetPassword(email, otp, newPassword);
		if (success) return ResponseEntity.ok("password reset successfully!!!");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("otp not verified or expired!!");
	}

	// primary role assignment during onboarding
	@PostMapping("/{userId}/setrole/{role}")
	public ResponseEntity<String> setRole(@PathVariable Long userId, @PathVariable Role role) {
		us.setPrimaryRole(userId, role);
		return ResponseEntity.status(HttpStatus.CREATED).body(role + " primary role set for this account!!!");
	}

	// multi-role: user checks "also register as Recruiter/Player" during onboarding
	@PostMapping("/{userId}/addrole/{role}")
	public ResponseEntity<String> addRole(@PathVariable Long userId, @PathVariable Role role) {
		us.addRole(userId, role);
		return ResponseEntity.status(HttpStatus.CREATED).body(role + " role added to this account!!!");
	}

	// role switcher dropdown reads this to know which roles the account holds
	@GetMapping("/{userId}/roles")
	public ResponseEntity<?> getRoles(@PathVariable Long userId) {
		return ResponseEntity.ok(us.getRoles(userId));
	}

	// friend search bar - search other users by name/gamer tag
	@GetMapping("/search")
	public List<User> searchUsers(@RequestParam String query) {
		return us.searchByNameOrTag(query);
	}

	@GetMapping("/getby/{id}")
	public ResponseEntity<?> getbyid(@PathVariable Long id){
		User data = us.getbyid(id);
		if(data != null) return ResponseEntity.ok(data);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found of this id!!");
	}

	@PatchMapping("/patch/{id}")
	public ResponseEntity<?> patchuser(@PathVariable long id, @RequestBody User usr) {
		User updated = us.patchuser(id, usr);
		if(updated!=null) return ResponseEntity.ok(updated);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found with the given id!!");
	}
}

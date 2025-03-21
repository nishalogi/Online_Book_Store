package com.bookstore.bookstore_backend.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.bookstore_backend.model.User;
import com.bookstore.bookstore_backend.model.UserStatus;
import com.bookstore.bookstore_backend.repository.UserRepository;
import com.bookstore.bookstore_backend.service.UserService;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
public class UserController {
	 private final UserRepository userRepository;
	    private final PasswordEncoder passwordEncoder;
	    private final UserService userService; // ✅ Added UserService dependency

	    // Constructor-based injection
	    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService) {
	        this.userRepository = userRepository;
	        this.passwordEncoder = passwordEncoder;
	        this.userService = userService; // ✅ Correctly initializing userService
	    }

	    // Register a normal user
	    @PostMapping("/register")
	    public ResponseEntity<String> registerUser(@RequestBody User user) {
	        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
	            return ResponseEntity.badRequest().body("Email already exists");
	        }
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	        user.setRole("ROLE_USER"); // Default role
	        userRepository.save(user);
	        return ResponseEntity.ok("User registered successfully!");
	    }

	    // Register an admin (only manually)
	    @PostMapping("/register-admin")
	    public ResponseEntity<String> registerAdmin(@RequestBody User user) {
	        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
	            return ResponseEntity.badRequest().body("Email already exists");
	        }
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	        user.setRole("ROLE_ADMIN"); // Set role as admin
	        userRepository.save(user);
	        return ResponseEntity.ok("Admin registered successfully!");
	    }

	    // ✅ Get all users (Fixed method call)
	    @GetMapping
	    public ResponseEntity<List<User>> getAllUsers() {
	        List<User> users = userService.getAllUsers(); // ✅ Use instance, not static reference
	        return ResponseEntity.ok(users);
	    }
	    @DeleteMapping("/{id}")
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
	        userRepository.deleteById(id);
	        return ResponseEntity.ok("User deleted successfully!");
	    }
	    
	    @PutMapping("/{id}/role") // this too
	    public ResponseEntity<?> updateUserRole(@PathVariable Long id, @RequestBody Map<String, String> request) {
	        String newRole = request.get("role");

	        // ✅ Ensure findById() returns a valid User object
	        User user = userService.findById(id).orElse(null);
	        if (user == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "User not found"));
	        }

	        user.setRole(newRole);
	        userService.save(user);

	        // ✅ Return a proper JSON response
	        return ResponseEntity.ok(Map.of("message", "User role updated successfully"));
	    }
	    
	    @PutMapping("/{id}/disable")
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<String> disableUser(@PathVariable Long id) {
	        userService.disableUser(id);
	        return ResponseEntity.ok("User disabled successfully!");
	    }
	    
	    @PutMapping("/{id}/status")
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<Map<String, String>> updateUserStatus(@PathVariable Long id, @RequestBody Map<String, String> requestBody) {
	        User user = userService.getUserById(id);

	        if (user == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("message", "❌ User not found"));
	        }

	        String status = requestBody.get("status");
	        if (status == null || (!status.equals("ACTIVE") && !status.equals("DISABLED"))) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("message", "❌ Invalid status value"));
	        }

	        user.setStatus(UserStatus.valueOf(status));
	        userService.save(user);

	        // ✅ Return a JSON object instead of plain text
	        return ResponseEntity.ok(Collections.singletonMap("message", "✅ User status updated successfully."));
	    }
}

package com.bookstore.bookstore_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bookstore.bookstore_backend.dto.SignupRequest;
import com.bookstore.bookstore_backend.model.User;
import com.bookstore.bookstore_backend.model.UserStatus;
import com.bookstore.bookstore_backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
	 private final UserRepository userRepository;
	    private final PasswordEncoder passwordEncoder;

	    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
	        this.userRepository = userRepository;
	        this.passwordEncoder = passwordEncoder;
	    }

	    /**
	     * Registers a new user with an encoded password.
	     */
	    public User registerUser(User user) {
	        // Check if email already exists
	        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
	        if (existingUser.isPresent()) {
	            throw new RuntimeException("Email is already in use.");
	        }

	        // Encode the password before saving
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	        return userRepository.save(user);
	    }

	    /**
	     * Finds a user by email.
	     */
	    public Optional<User> findByEmail(String email) {
	        return userRepository.findByEmail(email);
	    }

	    /**
	     * Retrieves a user by email. Returns User or throws an exception if not found.
	     */
	    public User getUserByEmail(String email) {
	        return userRepository.findByEmail(email)
	                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
	    }

	    public User registerUser(SignupRequest request) {
	        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
	            throw new RuntimeException("Email is already in use.");
	        }

	        User user = new User();
	        user.setName(request.getName());  // ✅ Added name field
	        user.setEmail(request.getEmail());
	        user.setPassword(passwordEncoder.encode(request.getPassword()));
	        user.setRole("ROLE_USER");  // Default role

	        return userRepository.save(user);
	    }
	    
	    public List<User> getAllUsers() {
	        return userRepository.findAll();
	    }
	    
	    public void deleteUser(Long userId) {
	        userRepository.deleteById(userId);
	    }

	    public void updateUserRole(Long userId, String role) { // i need to send this 
	        User user = userRepository.findById(userId)
	                .orElseThrow(() -> new RuntimeException("User not found"));
	        user.setRole(role);
	        userRepository.save(user);
	    }
	    
	    public Optional<User> findById(Long id) {  // ✅ Fix: Use Optional<User>
	        return userRepository.findById(id);
	    }

	    public void save(User user) {   // ✅ Ensure this method exists
	        userRepository.save(user);
	    }
	    
	    @Transactional
	    public void disableUser(Long userId) {
	        User user = userRepository.findById(userId)
	                .orElseThrow(() -> new RuntimeException("User not found!"));
	        
	        user.setStatus(UserStatus.DISABLED);
	        userRepository.save(user);
	    }
	    
	    public void changeUserStatus(Long userId, UserStatus newStatus) {
	        User user = userRepository.findById(userId).orElse(null);
	        if (user != null) {
	            user.setStatus(newStatus);
	            userRepository.save(user);
	        }
	    }
	    
	    public User getUserById(Long id) {
	        return userRepository.findById(id).orElse(null);
	    }




	    
	}

	  
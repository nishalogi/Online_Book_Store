package com.bookstore.bookstore_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.bookstore_backend.config.JwtUtil;
import com.bookstore.bookstore_backend.dto.AuthResponse;
import com.bookstore.bookstore_backend.dto.LoginRequest;
import com.bookstore.bookstore_backend.dto.SignupRequest;
import com.bookstore.bookstore_backend.model.User;
import com.bookstore.bookstore_backend.model.UserStatus;
import com.bookstore.bookstore_backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")  // Allow API to be accessed from frontend/Postman
public class AuthController {
	@Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        System.out.println("🔹 Login Attempt: " + loginRequest.getEmail());

        try {
            // Fetch user details first
            User user = userService.getUserByEmail(loginRequest.getEmail());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
            }

            //  Check if the user is disabled
            if (user.getStatus() == UserStatus.DISABLED) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Your account has been disabled. Contact support.");
            }

            //  Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtUtil.generateToken(authentication.getName());

            System.out.println("✅ Login Successful for: " + loginRequest.getEmail());

            // Return token, userId, and role
            return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getRole()));

        } catch (Exception e) {
            System.out.println("❌ Authentication Failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }
}

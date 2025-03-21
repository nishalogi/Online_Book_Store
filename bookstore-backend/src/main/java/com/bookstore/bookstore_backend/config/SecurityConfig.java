package com.bookstore.bookstore_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bookstore.bookstore_backend.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
public class SecurityConfig {
	 private final JwtFilter jwtFilter;
	    private final UserDetailsService userDetailsService;

	    public SecurityConfig(JwtFilter jwtFilter, UserDetailsService userDetailsService) {
	        this.jwtFilter = jwtFilter;
	        this.userDetailsService = userDetailsService;
	    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .cors().and()
	            .csrf(csrf -> csrf.disable())
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers("/api/auth/**").permitAll()  // Public authentication endpoints
	                .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll() 
	                .requestMatchers("/api/cart/**").authenticated()
	                .requestMatchers("/api/orders/place/**").authenticated()
	                .requestMatchers("/api/reviews/book/**").permitAll()
	                .requestMatchers("/api/reviews/add").authenticated()
	                .requestMatchers(HttpMethod.PUT, "/api/orders/{orderId}/cancel").authenticated()



	                // 🔒 Admin permissions (✅ FIX: use `hasAuthority("ROLE_ADMIN")` )
	                .requestMatchers(HttpMethod.POST, "/api/books/add").hasAuthority("ROLE_ADMIN")  
	                .requestMatchers(HttpMethod.PUT, "/api/books/**").hasAuthority("ROLE_ADMIN")   
	                .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasAuthority("ROLE_ADMIN") 
	                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")  
	                .requestMatchers(HttpMethod.PUT, "/api/orders/{orderId}/status").hasAuthority("ROLE_ADMIN")
	                .requestMatchers(HttpMethod.DELETE, "/api/reviews/delete/**").hasAuthority("ROLE_ADMIN")
	                .requestMatchers(HttpMethod.PUT, "/api/orders/**").hasRole("ADMIN")
	                .requestMatchers("/api/users/**").hasRole("ADMIN")
	                .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
	                .requestMatchers("/api/admin/**").hasRole("ADMIN")
	                .requestMatchers("/api/users/**").hasRole("ADMIN") 


	                // 🛒 User permissions
	                .requestMatchers("/api/cart/**").hasAuthority("ROLE_USER")  
	                .requestMatchers(HttpMethod.POST, "/api/orders/**").hasAuthority("ROLE_USER")  
	                .requestMatchers(HttpMethod.GET, "/api/orders/user/**").hasAuthority("ROLE_USER")  

	                // ✅ Allow authenticated users (both users & admins) to cancel their own orders
	                .requestMatchers(HttpMethod.PUT, "/api/orders/{orderId}/cancel").authenticated() 
	                .requestMatchers("/api/orders/**").authenticated()  
	                .requestMatchers("/api/reviews/**").authenticated()

	                .anyRequest().authenticated()
	            )
	            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

	        return http.build();
	    }

	    @Bean
	    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	        return authenticationConfiguration.getAuthenticationManager();
	    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	}

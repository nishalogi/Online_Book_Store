package com.bookstore.bookstore_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class AuthResponse {
	private String token;
    private Long userId;
    private String role;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	@Override
	public String toString() {
		return "AuthResponse [token=" + token + ", userId=" + userId + ", role=" + role + "]";
	}
	public AuthResponse(String token, Long userId, String role) {
		super();
		this.token = token;
		this.userId = userId;
		this.role = role;
	}
	
    

}

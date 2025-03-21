package com.bookstore.bookstore_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
	 private String name;
	    private String email;
	    private String password;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		@Override
		public String toString() {
			return "SignupRequest [name=" + name + ", email=" + email + ", password=" + password + "]";
		}
		public SignupRequest(String name, String email, String password) {
			super();
			this.name = name;
			this.email = email;
			this.password = password;
		}
		public SignupRequest() {
			super();
			// TODO Auto-generated constructor stub
		}
	    
	    
	    
	    

}

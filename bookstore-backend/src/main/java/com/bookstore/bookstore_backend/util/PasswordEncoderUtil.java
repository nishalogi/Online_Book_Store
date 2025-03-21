package com.bookstore.bookstore_backend.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bookstore.bookstore_backend.model.User;

public class PasswordEncoderUtil {

	public static void main(String[] args) {
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
//Change these values as needed
String name = "Nisha";  // Add user name
String email = "user@example.com";
String rawPassword = "password123";
String role = "ROLE_USER";

//Encode the password
String encodedPassword = encoder.encode(rawPassword);

//Create a new User object with name
User user = new User(null, name, email, encodedPassword, role);

//Print out the SQL query for insertion
System.out.println("INSERT INTO users (name, email, password, role) VALUES ('" 
 + user.getName() + "', '" 
 + user.getEmail() + "', '" 
 + user.getPassword() + "', '" 
 + user.getRole() + "');");

//Generate password hash for Admin
String adminName = "Admin";
String adminEmail = "logeshwarivijay16@gmail.com";
String adminPassword = "logeshwarinisha";
String adminEncodedPassword = encoder.encode(adminPassword);
String adminRole = "ROLE_ADMIN";

//Print SQL statements to insert into MySQL
System.out.println("\n--- SQL Statements to Insert Users ---");
System.out.println("INSERT INTO users (name, email, password, role) VALUES ('" 
 + adminName + "', '" + adminEmail + "', '" + adminEncodedPassword + "', '" + adminRole + "');");
	}
	
	

}

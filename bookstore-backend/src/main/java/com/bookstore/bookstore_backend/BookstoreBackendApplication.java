package com.bookstore.bookstore_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaRepositories("com.bookstore.bookstore_backend.repository")
@ComponentScan(basePackages = "com.bookstore.bookstore_backend")
public class BookstoreBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookstoreBackendApplication.class, args);
		System.out.println("Hello..SpringBoot!!");
	}
	
}

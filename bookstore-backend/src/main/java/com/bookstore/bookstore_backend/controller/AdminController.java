package com.bookstore.bookstore_backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.bookstore_backend.model.OrderStatus;
import com.bookstore.bookstore_backend.repository.BookRepository;
import com.bookstore.bookstore_backend.repository.OrderRepository;
import com.bookstore.bookstore_backend.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {
	 @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private OrderRepository orderRepository;

	    @Autowired
	    private BookRepository bookRepository;

	    // API to Get Dashboard Statistics
	    @GetMapping("/dashboard-stats")
	    @PreAuthorize("hasRole('ADMIN')")
	    public ResponseEntity<Map<String, Long>> getDashboardStats() {
	        Map<String, Long> stats = new HashMap<>();

	        stats.put("totalUsers", userRepository.count());
	        stats.put("totalOrders", orderRepository.count());
	        stats.put("totalBooks", bookRepository.count());
	        stats.put("pendingOrders", orderRepository.countByStatus(OrderStatus.PENDING));
	        stats.put("shippedOrders", orderRepository.countByStatus(OrderStatus.SHIPPED));  
	        stats.put("cancelledOrders", orderRepository.countByStatus(OrderStatus.CANCELLED)); 

	        return ResponseEntity.ok(stats);
	    }



}

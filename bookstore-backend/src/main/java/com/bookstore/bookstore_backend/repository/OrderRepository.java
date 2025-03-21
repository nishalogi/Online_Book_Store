package com.bookstore.bookstore_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.bookstore_backend.model.Order;
import com.bookstore.bookstore_backend.model.OrderStatus;
import com.bookstore.bookstore_backend.model.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Get all orders for a specific user
    List<Order> findByUser(User user);

	List<Order> findByUserId(Long userId);

	 Long countByStatus(OrderStatus status);

	void deleteByUserId(Long id);

	

	

}

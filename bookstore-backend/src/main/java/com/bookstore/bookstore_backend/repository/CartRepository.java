package com.bookstore.bookstore_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.bookstore_backend.model.Book;
import com.bookstore.bookstore_backend.model.Cart;
import com.bookstore.bookstore_backend.model.User;
@Repository
public interface CartRepository  extends JpaRepository<Cart, Long>{
	List<Cart> findByUser(User user);
	 // Delete all cart items for a user
    void deleteByUser(User user);
	Optional<Cart> findByUserAndBook(User user, Book book);
    
    

}

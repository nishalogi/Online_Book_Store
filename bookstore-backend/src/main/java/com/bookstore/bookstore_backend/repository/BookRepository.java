package com.bookstore.bookstore_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.bookstore_backend.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	//  Find books by category
    List<Book> findByCategory(String category);
    
    //  Search books by title (case-insensitive)
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    
    List<Book> findByQuantityLessThanEqual(int quantity);
    


}

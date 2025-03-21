package com.bookstore.bookstore_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.bookstore_backend.model.Book;
import com.bookstore.bookstore_backend.repository.BookRepository;

@Service
public class BookService {
	 @Autowired
	    private final BookRepository bookRepository;


	    public BookService(BookRepository bookRepository) {
	        this.bookRepository = bookRepository;
	    }

	    // Get all books
	    public List<Book> getAllBooks() {
	        return bookRepository.findAll();
	    }

	    // Get book by ID
	    public Optional<Book> getBookById(Long id) {
	        return bookRepository.findById(id);
	    }

	    // Add a new book
	    public Book addBook(Book book) {
	        return bookRepository.save(book);
	    }

	    // Update book details
	    public Book updateBook(Long id, Book updatedBook) {
	        return bookRepository.findById(id)
	                .map(existingBook -> {
	                    existingBook.setTitle(updatedBook.getTitle());
	                    existingBook.setAuthor(updatedBook.getAuthor());
	                    existingBook.setDescription(updatedBook.getDescription());
	                    existingBook.setPrice(updatedBook.getPrice());
	                    existingBook.setCategory(updatedBook.getCategory());
	                    existingBook.setQuantity(updatedBook.getQuantity());
	                    return bookRepository.save(existingBook);
	                })
	                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
	    }

	    // Delete a book by ID
	    public void deleteBook(Long id) {
	        bookRepository.deleteById(id);
	    }

	    // Find books by category
	    public List<Book> getBooksByCategory(String category) {
	        return bookRepository.findByCategory(category);
	    }

	    // Search books by title (case-insensitive, partial match)
	    public List<Book> searchBooksByTitle(String title) {
	        return bookRepository.findByTitleContainingIgnoreCase(title);
	    }
	    
	    public List<Book> findLowStockBooks() {
	        return bookRepository.findByQuantityLessThanEqual(5);
	    }
}

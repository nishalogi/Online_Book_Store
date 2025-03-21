package com.bookstore.bookstore_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.bookstore_backend.model.Book;
import com.bookstore.bookstore_backend.model.Review;
import com.bookstore.bookstore_backend.model.User;
import com.bookstore.bookstore_backend.repository.BookRepository;
import com.bookstore.bookstore_backend.repository.ReviewRepository;
import com.bookstore.bookstore_backend.repository.UserRepository;

@Service
public class ReviewService {
	 @Autowired
	    private ReviewRepository reviewRepository;

	    @Autowired
	    private BookRepository bookRepository;

	    @Autowired
	    private UserRepository userRepository;

	    // ✅ Add a new review
	    public Review addReview(Long userId, Long bookId, int rating, String comment) {
	        Optional<User> user = userRepository.findById(userId);
	        Optional<Book> book = bookRepository.findById(bookId);

	        if (user.isPresent() && book.isPresent()) {
	            Review review = new Review();
	            review.setUser(user.get());
	            review.setBook(book.get());
	            review.setRating(rating);
	            review.setComment(comment);
	            return reviewRepository.save(review);
	        } else {
	            throw new RuntimeException("User or Book not found");
	        }
	    }

	    // ✅ Get all reviews for a book
	    public List<Review> getReviewsByBook(Long bookId) {
	        Optional<Book> book = bookRepository.findById(bookId);
	        return book.map(reviewRepository::findByBook).orElseThrow(() -> new RuntimeException("Book not found"));
	    }

	    // ✅ Delete a review (admin only)
	    public void deleteReview(Long reviewId) {
	        reviewRepository.deleteById(reviewId);
	    }

}

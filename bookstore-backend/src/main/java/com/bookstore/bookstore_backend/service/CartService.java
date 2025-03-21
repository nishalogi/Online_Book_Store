package com.bookstore.bookstore_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.bookstore_backend.model.Book;
import com.bookstore.bookstore_backend.model.Cart;
import com.bookstore.bookstore_backend.model.User;
import com.bookstore.bookstore_backend.repository.BookRepository;
import com.bookstore.bookstore_backend.repository.CartRepository;
import com.bookstore.bookstore_backend.repository.UserRepository;
@Service
public class CartService {
	 @Autowired
	    private CartRepository cartRepository;

	    @Autowired
	    private UserRepository userRepository;  // ✅ Inject UserRepository

	    @Autowired
	    private BookRepository bookRepository;  // ✅ Inject BookRepository

	    // ✅ Get cart items for the logged-in user
	    public List<Cart> getCartItems(User user) {
	        List<Cart> cartItems = cartRepository.findByUser(user);
	        
	        // ✅ Ensure book details are correctly included
	        cartItems.forEach(cart -> {
	            if (cart.getBook() == null) {
	                throw new RuntimeException("Book data missing for cart item: " + cart.getId());
	            }
	        });

	        return cartItems;
	    }


	    // ✅ Add item to cart with proper user and book validation
	    public Cart addToCart(Long userId, Long bookId, int quantity) {
	        // Fetch user from database
	        User user = userRepository.findById(userId)
	            .orElseThrow(() -> new RuntimeException("User not found!"));

	        // Fetch book from database
	        Book book = bookRepository.findById(bookId)
	            .orElseThrow(() -> new RuntimeException("Book not found!"));

	        // Check if the book is already in the cart
	        Optional<Cart> existingCartItem = cartRepository.findByUserAndBook(user, book);
	        if (existingCartItem.isPresent()) {
	            // If book exists, update the quantity
	            Cart cart = existingCartItem.get();
	            cart.setQuantity(cart.getQuantity() + quantity);
	            return cartRepository.save(cart);
	        } else {
	            // If book does not exist, create a new cart item
	            Cart cart = new Cart();
	            cart.setUser(user);
	            cart.setBook(book);
	            cart.setQuantity(quantity);
	            return cartRepository.save(cart);
	        }
	    }

	    // ✅ Remove item from cart (Ensures user can only delete their own cart item)
	    public void removeFromCart(Long cartId, User user) {
	        Optional<Cart> cartItem = cartRepository.findById(cartId);

	        if (cartItem.isPresent() && cartItem.get().getUser().getId().equals(user.getId())) {
	            cartRepository.deleteById(cartId);
	        } else {
	            throw new RuntimeException("Unauthorized to delete this cart item!");
	        }
	    }

	    // ✅ Clear all cart items for a specific user
	    public void clearCart(User user) {
	        List<Cart> userCartItems = cartRepository.findByUser(user);
	        cartRepository.deleteAll(userCartItems);
	    }


	    public void removeItem(Long cartItemId) {
	        cartRepository.deleteById(cartItemId);
	    }
	    
	    public void updateCartItem(Long cartItemId, int quantity, Long userId) {
	        Cart cartItem = cartRepository.findById(cartItemId)
	                .orElseThrow(() -> new RuntimeException("Cart item not found"));

	        if (!cartItem.getUser().getId().equals(userId)) {
	            throw new RuntimeException("Unauthorized action!"); // Prevent users from updating others' carts
	        }

	        cartItem.setQuantity(quantity);
	        cartRepository.save(cartItem);
	    }

	}
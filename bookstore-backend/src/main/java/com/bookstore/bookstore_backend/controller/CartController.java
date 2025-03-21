package com.bookstore.bookstore_backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bookstore.bookstore_backend.model.Cart;
import com.bookstore.bookstore_backend.model.User;
import com.bookstore.bookstore_backend.repository.UserRepository;
import com.bookstore.bookstore_backend.service.CartService;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class CartController {
	@Autowired
    private CartService cartService;

    @Autowired
    private UserRepository userRepository;

    // ✅ Get cart items for logged-in user
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<Cart> cartItems = cartService.getCartItems(user.get());
        
        // ✅ Build correct JSON response
        List<Map<String, Object>> response = cartItems.stream().map(cart -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", cart.getId());
            item.put("quantity", cart.getQuantity());
            
            // ✅ Ensure correct book data is used
            Map<String, Object> bookData = new HashMap<>();
            bookData.put("id", cart.getBook().getId());
            bookData.put("title", cart.getBook().getTitle());
            bookData.put("author", cart.getBook().getAuthor());
            bookData.put("price", cart.getBook().getPrice());
            bookData.put("imageUrl", cart.getBook().getImageUrl());

            item.put("book", bookData);

            return item;
        }).toList();

        return ResponseEntity.ok(response);
    }


    // ✅ Add book to cart using JSON body
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> request, 
                                       @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        try {
            Long bookId = ((Number) request.get("bookId")).longValue();
            Integer quantity = (Integer) request.get("quantity");

            Cart cartItem = cartService.addToCart(user.get().getId(), bookId, quantity);
            return ResponseEntity.ok(cartItem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        }
    }

    // ✅ Remove item from cart
    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long cartItemId) {
        cartService.removeItem(cartItemId);
        return ResponseEntity.ok().build();
    }


    // ✅ Clear entire cart for the logged-in user
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        if (user.isPresent()) {
            cartService.clearCart(user.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // User not found
        }
    }
    
    
    @PutMapping("/{cartItemId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable Long cartItemId, 
            @RequestBody Map<String, Object> request, 
            @AuthenticationPrincipal UserDetails userDetails) {
        
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User not authenticated");
        }

        Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        try {
            Integer quantity = (Integer) request.get("quantity");

            cartService.updateCartItem(cartItemId, quantity, user.get().getId());
            return ResponseEntity.ok().body("Cart item updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: " + e.getMessage());
        }
    }

    
}
package com.bookstore.bookstore_backend.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import com.bookstore.bookstore_backend.dto.OrderRequest;
import com.bookstore.bookstore_backend.dto.OrderResponse;
import com.bookstore.bookstore_backend.model.Order;
import com.bookstore.bookstore_backend.model.OrderStatus;
import com.bookstore.bookstore_backend.service.EmailService;
import com.bookstore.bookstore_backend.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {
	private final OrderService orderService;
    private final EmailService emailService;

    @Autowired
    public OrderController(OrderService orderService, EmailService emailService) {
        this.orderService = orderService;
        this.emailService = emailService;
    }

    //  Place an order
    @PostMapping("/place/{userId}")
    public ResponseEntity<OrderResponse> placeOrder(@PathVariable Long userId, @RequestBody OrderRequest request) {
        OrderResponse orderResponse = orderService.createOrder(
            userId, 
            request.getBookIds(), 
            request.getQuantities(),
            request.getAddress(),
            request.getPaymentMethod(),
            request.getPhoneNumber()
        );
        return ResponseEntity.ok(orderResponse);
    }

    //  Get orders for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getUserOrders(@PathVariable Long userId) {
        List<OrderResponse> orderResponses = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orderResponses);
    }

    //  Get order details by ID
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        OrderResponse orderResponse = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderResponse);
    }

    //  Cancel an order (Only owner or admin can cancel)
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(
            @PathVariable Long orderId, 
            @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        String userEmail = userDetails.getUsername();

        try {
            boolean isCancelled = orderService.cancelOrder(orderId, userEmail);

            if (isCancelled) {
                emailService.sendOrderCancellationEmail(userEmail, orderId.toString());
                return ResponseEntity.ok("Order cancelled successfully. Email notification sent.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order cannot be canceled.");
            }
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
        }
    }

    //  Get all orders (Admin only)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orderResponses = orderService.getAllOrders();
        return ResponseEntity.ok(orderResponses);
    }

    //  Update order status (Admin only)
    @PutMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        OrderResponse updatedOrder = orderService.updateOrderStatus(orderId, status);

        //  If order is marked as "SHIPPED", send email notification
        if (status == OrderStatus.SHIPPED) {
            orderService.sendOrderShippedEmail(orderId);
        }

        return ResponseEntity.ok(updatedOrder);
    }


    //  Delete order
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrderById(id);
        return ResponseEntity.ok("Order deleted successfully");
    }
    
    
    
}

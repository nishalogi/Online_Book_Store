package com.bookstore.bookstore_backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.bookstore_backend.dto.OrderResponse;
import com.bookstore.bookstore_backend.model.Book;
import com.bookstore.bookstore_backend.model.Order;
import com.bookstore.bookstore_backend.model.OrderStatus;
import com.bookstore.bookstore_backend.model.PaymentMethod;
import com.bookstore.bookstore_backend.model.User;
import com.bookstore.bookstore_backend.repository.BookRepository;
import com.bookstore.bookstore_backend.repository.OrderRepository;
import com.bookstore.bookstore_backend.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
	private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final EmailService emailService;

    @Autowired
    public OrderService(OrderRepository orderRepository, 
                        UserRepository userRepository, 
                        BookRepository bookRepository, 
                        EmailService emailService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.emailService = emailService;
    }

    public OrderResponse createOrder(Long userId, List<Long> bookIds, List<Integer> quantities, String address, PaymentMethod paymentMethod,String PhoneNumber) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        double totalPrice = 0;
        List<Book> books = new ArrayList<>();

        for (int i = 0; i < bookIds.size(); i++) {
            Book book = bookRepository.findById(bookIds.get(i))
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            books.add(book);
            totalPrice += book.getPrice() * quantities.get(i);
        }

        Order order = new Order();
        order.setUser(user);
        order.setBookIds(bookIds);
        order.setQuantities(quantities);
        order.setTotalPrice(totalPrice);
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setAddress(address);
        order.setPhoneNumber(PhoneNumber);
        order.setPaymentMethod(paymentMethod);

        Order savedOrder = orderRepository.save(order);

        sendOrderConfirmationEmail(savedOrder);

        return new OrderResponse(savedOrder, books);
    }

    public List<OrderResponse> getOrdersByUser(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        List<Book> books = bookRepository.findAllById(order.getBookIds());
        return new OrderResponse(order, books);
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) { 
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        return convertToOrderResponse(updatedOrder);
    }

    public boolean cancelOrder(Long orderId, String userEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isAdmin = user.getRole().equals("ROLE_ADMIN");
        boolean isOwner = order.getUser().getEmail().equals(userEmail);

        if (!isAdmin && !isOwner) {
            throw new RuntimeException("You are not authorized to cancel this order");
        }

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new RuntimeException("Only pending orders can be canceled");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        sendOrderCancellationEmail(order);

        return true;
    }

    @Transactional
    public void deleteOrderById(Long orderId) {
        if (orderRepository.existsById(orderId)) {
            orderRepository.deleteById(orderId);
        } else {
            throw new RuntimeException("Order not found with ID: " + orderId);
        }
    }

    private void sendOrderConfirmationEmail(Order order) {
        String subject = "Order Confirmation - Bookstore";

        // Fetch book names based on bookIds in the order
        List<String> bookNames = order.getBookIds().stream()
                .map(bookId -> bookRepository.findById(bookId)
                        .map(Book::getTitle)
                        .orElse("Unknown Book")) // Handle cases where the book might not exist
                .collect(Collectors.toList());

        String bookList = String.join(", ", bookNames); // Convert list to a readable string

        String body = "Dear " + order.getUser().getName() + ",\n\n"
                    + "Thank you for your order! Your order contains the following book(s):\n"
                    + bookList + "\n\n"
                    + "We will notify you once your order is shipped.\n\n"
                    + "Best Regards,\nBookstore Team";

        emailService.sendEmail(order.getUser().getEmail(), subject, body);
    }


    private void sendOrderCancellationEmail(Order order) {
        String subject = "Order Cancellation - Bookstore";

        // Fetch book names based on bookIds in the order
        List<String> bookNames = order.getBookIds().stream()
                .map(bookId -> bookRepository.findById(bookId)
                        .map(Book::getTitle)
                        .orElse("Unknown Book")) // Handle cases where the book might not exist
                .collect(Collectors.toList());

        String bookList = String.join(", ", bookNames); // Convert list to a readable string

        String body = "Dear " + order.getUser().getName() + ",\n\n"
                    + "Your order containing the following book(s) has been cancelled successfully:\n"
                    + bookList + "\n\n"
                    + "If you have any questions, please contact support.\n\n"
                    + "Best Regards,\nBookstore Team";

        emailService.sendEmail(order.getUser().getEmail(), subject, body);
    }
    
    public void sendOrderShippedEmail(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        String subject = "Your Order Has Been Shipped - Bookstore";

        // Fetch book names based on bookIds in the order
        List<String> bookNames = order.getBookIds().stream()
                .map(bookId -> bookRepository.findById(bookId)
                        .map(Book::getTitle)
                        .orElse("Unknown Book"))
                .collect(Collectors.toList());

        String bookList = String.join(", ", bookNames); // Convert list to a readable string

        String body = "Dear " + order.getUser().getName() + ",\n\n"
                    + "We are excited to inform you that your order containing the following book(s) has been shipped:\n"
                    + bookList + "\n\n"
                    + "Your order is on its way and will be delivered soon!\n\n"
                    + "If you have any questions, please contact our support team.\n\n"
                    + "Best Regards,\nBookstore Team";

        emailService.sendEmail(order.getUser().getEmail(), subject, body);
    }

 

    private OrderResponse convertToOrderResponse(Order order) {
        List<Book> books = bookRepository.findAllById(order.getBookIds());
        return new OrderResponse(order, books);
    }
    
    
    
    
    
}	
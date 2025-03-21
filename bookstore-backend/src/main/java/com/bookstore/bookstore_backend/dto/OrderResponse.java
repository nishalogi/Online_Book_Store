package com.bookstore.bookstore_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.bookstore.bookstore_backend.model.Book;
import com.bookstore.bookstore_backend.model.Order;
import com.bookstore.bookstore_backend.model.OrderStatus;
import com.bookstore.bookstore_backend.model.PaymentMethod;
import com.bookstore.bookstore_backend.model.User;

public class OrderResponse {
	private Long orderId;
    private User user;
    private List<Book> books;  // Book details included
    private List<Integer> quantities;
    private double totalPrice;
    private String address;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private PaymentMethod paymentMethod;
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> books) {
		this.books = books;
	}
	public List<Integer> getQuantities() {
		return quantities;
	}
	public void setQuantities(List<Integer> quantities) {
		this.quantities = quantities;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public LocalDateTime getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public OrderResponse(Long orderId, User user, List<Book> books, List<Integer> quantities, double totalPrice,
			String address, OrderStatus status, LocalDateTime orderDate, PaymentMethod paymentMethod) {
		super();
		this.orderId = orderId;
		this.user = user;
		this.books = books;
		this.quantities = quantities;
		this.totalPrice = totalPrice;
		this.address = address;
		this.status = status;
		this.orderDate = orderDate;
		this.paymentMethod = paymentMethod;
	}
	@Override
	public String toString() {
		return "OrderResponse [orderId=" + orderId + ", user=" + user + ", books=" + books + ", quantities="
				+ quantities + ", totalPrice=" + totalPrice + ", address=" + address + ", status=" + status
				+ ", orderDate=" + orderDate + ", paymentMethod=" + paymentMethod + "]";
	}
	public OrderResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	 public OrderResponse(Order order, List<Book> books) {
	        this.orderId = order.getId();
	        this.user = order.getUser();
	        this.books = books;
	        this.quantities = order.getQuantities();
	        this.totalPrice = order.getTotalPrice();
	        this.address = order.getAddress();
	        this.status = order.getStatus();
	        this.orderDate = order.getOrderDate();
	        this.paymentMethod = order.getPaymentMethod();
	    }
    
    
    
    
}
	
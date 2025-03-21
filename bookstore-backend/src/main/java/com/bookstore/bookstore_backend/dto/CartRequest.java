package com.bookstore.bookstore_backend.dto;

import com.bookstore.bookstore_backend.model.Book;

public class CartRequest {
	private Long bookId;
    private int quantity;
	public Long getBookId() {
		return bookId;
	}
	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "CartRequest [bookId=" + bookId + ", quantity=" + quantity + "]";
	}
	public CartRequest(Long bookId, int quantity) {
		super();
		this.bookId = bookId;
		this.quantity = quantity;
	}
	public CartRequest() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	
    

}

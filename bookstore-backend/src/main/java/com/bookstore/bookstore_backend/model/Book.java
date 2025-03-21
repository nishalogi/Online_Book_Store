package com.bookstore.bookstore_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class Book {
	    @Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;
        private String title;
	    private String author;
	    private String description;
	    private double price;
	    private String category;
	    private Long quantity;
	    private String imageUrl;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getAuthor() {
			return author;
		}
		public void setAuthor(String author) {
			this.author = author;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public double getPrice() {
			return price;
		}
		public void setPrice(double price) {
			this.price = price;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public Long getQuantity() {
			return quantity;
		}
		public void setQuantity(Long quantity) {
			this.quantity = quantity;
		}
		public String getImageUrl() {
			return imageUrl;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		@Override
		public String toString() {
			return "Book [id=" + id + ", title=" + title + ", author=" + author + ", description=" + description
					+ ", price=" + price + ", category=" + category + ", quantity=" + quantity + ", imageUrl="
					+ imageUrl + "]";
		}
		public Book(Long id, String title, String author, String description, double price, String category,
				Long quantity, String imageUrl) {
			super();
			this.id = id;
			this.title = title;
			this.author = author;
			this.description = description;
			this.price = price;
			this.category = category;
			this.quantity = quantity;
			this.imageUrl = imageUrl;
		}
		public Book() {
			super();
			// TODO Auto-generated constructor stub
		}
}
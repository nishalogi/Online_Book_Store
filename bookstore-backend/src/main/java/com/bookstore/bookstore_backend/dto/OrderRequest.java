package com.bookstore.bookstore_backend.dto;

import java.util.List;

import com.bookstore.bookstore_backend.model.PaymentMethod;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
	 private List<Long> bookIds;
	    private List<Integer> quantities;
	    private String address;
	    private PaymentMethod paymentMethod;
	    private String cancelUrl;  
	    private String successUrl;
	    private String phoneNumber;
	    
		public String getPhoneNumber() {
			return phoneNumber;
		}
		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}
		public List<Long> getBookIds() {
			return bookIds;
		}
		public void setBookIds(List<Long> bookIds) {
			this.bookIds = bookIds;
		}
		public List<Integer> getQuantities() {
			return quantities;
		}
		public void setQuantities(List<Integer> quantities) {
			this.quantities = quantities;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public PaymentMethod getPaymentMethod() {
			return paymentMethod;
		}
		public void setPaymentMethod(PaymentMethod paymentMethod) {
			this.paymentMethod = paymentMethod;
		}
		public String getCancelUrl() {
			return cancelUrl;
		}
		public void setCancelUrl(String cancelUrl) {
			this.cancelUrl = cancelUrl;
		}
		public String getSuccessUrl() {
			return successUrl;
		}
		public void setSuccessUrl(String successUrl) {
			this.successUrl = successUrl;
		}
		@Override
		public String toString() {
			return "OrderRequest [bookIds=" + bookIds + ", quantities=" + quantities + ", address=" + address
					+ ", paymentMethod=" + paymentMethod + ", cancelUrl=" + cancelUrl + ", successUrl=" + successUrl
					+ "]";
		}
		public OrderRequest(List<Long> bookIds, List<Integer> quantities, String address, PaymentMethod paymentMethod,
				String cancelUrl, String successUrl) {
			super();
			this.bookIds = bookIds;
			this.quantities = quantities;
			this.address = address;
			this.paymentMethod = paymentMethod;
			this.cancelUrl = cancelUrl;
			this.successUrl = successUrl;
		}
		public OrderRequest() {
			super();
			// TODO Auto-generated constructor stub
		}
	    
	    

}
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private baseUrl = 'http://localhost:8085/api/cart';

  constructor(private http: HttpClient) {}

  // Get JWT token from local storage
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    });
  }



  // Get cart items
  getCartItems(): Observable<any> {
    return this.http
      .get(`${this.baseUrl}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError));
  }

  // Add book to cart


  addToCart(cartItem: { bookId: number; quantity: number }) {
    const token = localStorage.getItem('token'); // Assuming you store JWT in localStorage
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`, // 🔒 Send JWT token
    });
  
    return this.http.post(`http://localhost:8085/api/cart/add`, cartItem, { headers });
  }
  
  

  // Update cart item quantity
  updateCartItem(cartItemId: number, quantity: number): Observable<any> {
    const url = `http://localhost:8085/api/cart/${cartItemId}`;
    return this.http.put(url, { quantity }, { headers: this.getAuthHeaders(), responseType: 'text' });
  }
  

  // Remove item from cart
  removeCartItem(cartItemId: number): Observable<any> {
    return this.http
      .delete(`${this.baseUrl}/remove/${cartItemId}`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError));
  }

  // Clear entire cart
  clearCart(): Observable<any> {
    return this.http
      .delete(`${this.baseUrl}/clear`, { headers: this.getAuthHeaders() })
      .pipe(catchError(this.handleError));
  }

  // Handle errors
  private handleError(error: any) {
    console.error('Error in CartService:', error);
    return throwError(() => new Error(error.error || 'Something went wrong'));
  }


  
  }


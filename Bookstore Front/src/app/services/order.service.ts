import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { Order } from '../models/order.model';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = 'http://localhost:8085/api/orders'; // ✅ Centralized API URL



  constructor(private http: HttpClient, private authService: AuthService) {}

  // ✅ Helper function to generate authentication headers
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token') || sessionStorage.getItem('token');
    if (!token) {
      console.warn('No authentication token found');
      return new HttpHeaders();
    }
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }

  // ✅ Fetch all orders for a logged-in user
  getUserOrders(): Observable<any[]> {
    const userId = this.getUserId();
    if (!userId) return of([]); // Prevents request if userId is missing

    return this.http.get<any[]>(`${this.apiUrl}/user/${userId}`, {
      headers: this.getAuthHeaders(),
      withCredentials: true
    }).pipe(
      catchError(this.handleError<any[]>('getUserOrders', []))
    );
  }

  // ✅ Fetch specific order details
  getOrderById(orderId: number): Observable<any> {
    if (!orderId || isNaN(orderId)) {
      console.error('Invalid Order ID:', orderId);
      return of(null);
    }

    return this.http.get<any>(`${this.apiUrl}/${orderId}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError<any>('getOrderById'))
    );
  }

  // ✅ Cancel an order
  cancelOrder(orderId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/${orderId}/cancel`, null, {
      headers: this.getAuthHeaders(),
    }).pipe(
      catchError(this.handleError<any>('cancelOrder'))
    );
  }

  placeOrder(userId: number, orderData: any): Observable<any> {
    const token = localStorage.getItem('token'); // Retrieve JWT token from local storage
    if (!token) {
      console.error('❌ No authentication token found');
      return new Observable(observer => {
        observer.error('User not authenticated');
        observer.complete();
      });
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`, // Add the JWT token in the request header
      'Content-Type': 'application/json'
    });

    return this.http.post(`${this.apiUrl}/place/${userId}`, orderData, { headers });
  }

  
  

  // ✅ Delete an order
  deleteOrder(orderId: number): Observable<null> {
    if (!orderId || isNaN(orderId)) {
      console.error('Invalid Order ID:', orderId);
      return of(null);
    }

    return this.http.delete<null>(`${this.apiUrl}/${orderId}`, {
      headers: this.getAuthHeaders()
    }).pipe(
      catchError(this.handleError<null>('deleteOrder'))
    );
  }

  // ✅ Helper function to get User ID
  private getUserId(): number | null {
    const userIdStr = localStorage.getItem('userId');
    if (!userIdStr) {
      console.warn('User ID is missing');
      return null;
    }
    const userId = parseInt(userIdStr, 10);
    if (isNaN(userId)) {
      console.warn('Invalid User ID');
      return null;
    }
    return userId;
  }

  // ✅ Generalized Error Handling
  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(`${operation} failed:`, error);
      return of(result as T);
    };
  }

  updateOrderStatus(orderId: number, status: string): Observable<any> {
    const token = localStorage.getItem('token'); // ✅ Get JWT token from local storage
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`, // ✅ Attach token
    });
  
    return this.http.put(`${this.apiUrl}/${orderId}/status?status=${status}`, {}, { headers });
  }
  

  getAllOrders(): Observable<Order[]> {
    const token = localStorage.getItem('token'); // Fetch token from local storage
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`, // Attach token in the request
    });
  
    return this.http.get<Order[]>(`${this.apiUrl}`, { headers });
  }

}


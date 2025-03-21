import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8085/api/auth'; // Adjust API URL if needed

  constructor(private http: HttpClient) {}

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token'); // ✅ Checks if a token exists
  }



  login(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials);
  }

  signup(user: { name: string; email: string; password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/signup`, user);
  }

  // ✅ Check if localStorage is available before using it
  private isLocalStorageAvailable(): boolean {
    return typeof localStorage !== 'undefined';
  }

  logout() {
    localStorage.removeItem('token');
  }

  // ✅ Save user data to localStorage
  saveUserData(userData: any): void {
    if (this.isLocalStorageAvailable()) {
      localStorage.setItem('user', JSON.stringify(userData));
      if (userData.userId) {
        localStorage.setItem('userId', userData.userId.toString()); 
      }
    }
  }

  // ✅ Retrieve user data from localStorage
  getUserData(): any {
    if (this.isLocalStorageAvailable()) {
      const userData = localStorage.getItem('user');
      return userData ? JSON.parse(userData) : null;
    }
    return null;
  }

  // ✅ Get User ID correctly
  getUserId(): number | null {
    if (this.isLocalStorageAvailable()) {
      const userId = localStorage.getItem('userId');
      return userId ? parseInt(userId, 10) : null;
    }
    return null;
  }

  // ✅ Clear user data from localStorage (use for logout)
  clearUserData(): void {
    if (this.isLocalStorageAvailable()) {
      localStorage.removeItem('user');
      localStorage.removeItem('userId');
    }
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isAdmin(): boolean {
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    return user && user.role === 'ADMIN';
  }

  
  
}

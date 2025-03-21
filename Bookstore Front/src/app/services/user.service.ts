import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';  // Import User model

@Injectable({
    providedIn: 'root'
})
export class UserService {
    private apiUrl = 'http://localhost:8085/api/users';

    constructor(private http: HttpClient) { }

    getAllUsers(): Observable<any> {
        const token = localStorage.getItem('token'); // Retrieve token from storage
        const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

        return this.http.get(this.apiUrl, { headers });
    }

    deleteUser(userId: number): Observable<any> {
        const headers = new HttpHeaders({
            'Authorization': `Bearer ${localStorage.getItem('token')}` // Get the token
        });

        return this.http.delete(`${this.apiUrl}/${userId}`, { headers });
    }



    // ✅ Update user role with proper headers
    updateUserRole(userId: number, role: string): Observable<any> {
        return this.http.put(`${this.apiUrl}/${userId}/role`, { role }, { headers: this.getAuthHeaders() });
    }

    // ✅ Get Auth Headers (Fixes JWT Issues)
    private getAuthHeaders(): HttpHeaders {
        const token = localStorage.getItem('token');  // Ensure correct key
        if (!token) {
            console.error("No token found in local storage! User might not be logged in.");
            return new HttpHeaders(); // Return empty headers to avoid sending an invalid token
        }
        return new HttpHeaders({ 'Authorization': `Bearer ${token}` });
    }

    updateUserStatus(userId: number, status: string) {
        return this.http.put(`${this.apiUrl}/${userId}/status`, { status }, { headers: this.getAuthHeaders() });
    }

    changeUserStatus(userId: number, status: string): Observable<any> {
        const headers = new HttpHeaders({
            'Authorization': 'Bearer ' + localStorage.getItem('token'), // Ensure JWT token is included
            'Content-Type': 'application/json'
        });

        return this.http.put(
            `${this.apiUrl}/users/${userId}/status`,
            { status: status }, // Ensure status is sent as a JSON object
            { headers }
        );
    }

}



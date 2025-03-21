import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SignupService {
  private baseUrl = 'http://localhost:8085/api/auth/signup'; // Update with your backend URL

  constructor(private http: HttpClient) {}

  signup(userData: any): Observable<any> {
    return this.http.post(this.baseUrl, userData);
  }
}


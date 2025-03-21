import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private baseUrl = 'http://localhost:8085/api/reviews';

  constructor(private http: HttpClient) {}

  private getAuthHeaders() {
    const token = localStorage.getItem('token'); // Get token from local storage
    return token
      ? { headers: new HttpHeaders({ 'Authorization': `Bearer ${token}` }) }
      : {};
  }

  // Fetch reviews for a book
  getReviewsByBookId(bookId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/book/${bookId}`, this.getAuthHeaders());
  }


  addReview(bookId: number, userId: number, rating: number, comment: string): Observable<any> {
    const body = {bookId,userId,rating,comment};
    return this.http.post<any>(`${this.baseUrl}/add`,body,this.getAuthHeaders());
  }
}


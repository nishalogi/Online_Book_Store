import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from '../models/book.model';

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private apiUrl = 'http://localhost:8085/api/books';
  
  constructor(private http: HttpClient) {}

  //  Fetch all books (for book list page)
  getBooks(): Observable<any> {
    return this.http.get(`${this.apiUrl}`);
  }

  //  Fetch a single book by ID (for book details page)
  getBookById(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }

  getAllBooks(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}`);
  }


  deleteBook(bookId: number, headers: HttpHeaders): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${bookId}`, { headers });
  }
  
  
  updateBook(book: any): Observable<any> {
    const token = localStorage.getItem('token');  // Get JWT token
    if (!token) {
      console.error("No token found, user may not be logged in.");
      return new Observable();  // Return empty observable to prevent error
    }
  
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`, // Include JWT token
      'Content-Type': 'application/json'
    });
  
    return this.http.put(`${this.apiUrl}/${book.id}`, book, { headers });
  }

  addBook(book: Book): Observable<Book> {
    const token = localStorage.getItem('token');
    
    console.log("Sending Token:", token);  // ✅ Log token
    console.log("Adding Book:", book);  // ✅ Log book details
  
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  
    return this.http.post<Book>(`${this.apiUrl}/add`, book, { headers });
  }
  
  

  
  
  

}


import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl = 'http://localhost:8085/api/admin/dashboard-stats';

  // Inject HttpClient in the constructor
  constructor(private http: HttpClient) {}

  getDashboardStats(): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': 'Bearer ' + localStorage.getItem('token')
    });

    return this.http.get<any>(this.apiUrl, { headers });
  }

  getLowStockBooks() {
    return this.http.get<any[]>('http://localhost:8085/api/books/low-stock');
  }
  

  
}




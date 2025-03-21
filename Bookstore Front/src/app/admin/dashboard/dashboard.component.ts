import { Component, OnInit } from '@angular/core';
import { AdminService } from '../admin.service';

@Component({
  selector: 'app-dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  dashboardStats: any = {
    totalBooks: 0,
    totalUsers: 0,
    totalOrders: 0,
    pendingOrders: 0,
    shippedOrders: 0,
    cancelledOrders: 0
  };

  lowStockBooks: any[] = []; // List to store low stock books

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadDashboardStats();
    this.loadLowStockBooks();
  }

  loadDashboardStats(): void {
    this.adminService.getDashboardStats().subscribe({
      next: (data) => {
        this.dashboardStats = {
          totalBooks: data.totalBooks || 0,
          totalUsers: data.totalUsers || 0,
          totalOrders: data.totalOrders || 0,
          pendingOrders: data.pendingOrders || 0,
          shippedOrders: data.shippedOrders || 0,
          cancelledOrders: data.cancelledOrders || 0
        };
      },
      error: (error) => {
        console.error('Error fetching dashboard stats:', error);
      }
    });
  }

  loadLowStockBooks(): void {
    this.adminService.getLowStockBooks().subscribe({
      next: (books) => {
        this.lowStockBooks = books;
        console.log('Low Stock Books:', this.lowStockBooks);
      },
      error: (error) => {
        console.error('Error fetching low stock books:', error);
      }
    });
  }
}


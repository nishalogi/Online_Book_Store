import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OrderService } from '../../services/order.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-orders',
  standalone: false,
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {
  orders: any[] = [];
  userId: number | null = null;
  loading: boolean = true;
  errorMessage: string = '';

  constructor(
    private orderService: OrderService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userId = this.getUserId();
    if (this.userId !== null) {
      this.fetchOrders();
    } else {
      this.handleUserIdError();
    }
  }

  // ✅ Fetch Orders
  fetchOrders(): void {
    this.loading = true;
    this.orderService.getUserOrders().subscribe(
      (data) => {
        console.log('Received orders:', data);
        this.orders = data.map(order => ({
          ...order,
          bookTitles: order.books ? order.books.map((book: any) => book.title).join(', ') : 'No Books'
        }));
        this.errorMessage = this.orders.length > 0 ? '' : 'No orders found.';
        this.loading = false;
      },
      (error) => {
        this.loading = false;
        this.errorMessage = `Error fetching orders: ${error.message}`;
        console.error('Error fetching orders:', error);
      }
    );
  }
  

  // ✅ Get User ID from Storage
  private getUserId(): number | null {
    const storedUserId = localStorage.getItem('userId');
    if (!storedUserId) {
      console.warn('User ID is missing from storage.');
      return null;
    }
    const userId = parseInt(storedUserId, 10);
    if (isNaN(userId)) {
      console.warn('Invalid User ID:', storedUserId);
      return null;
    }
    return userId;
  }

  // ✅ Handle User ID Errors
  private handleUserIdError(): void {
    this.loading = false;
    this.errorMessage = 'User ID is missing or invalid. Please log in.';
    console.error(this.errorMessage);
  }

  // ✅ View Order Details
  viewOrder(orderId: number): void {
    if (!orderId) {
      console.error("Invalid orderId:", orderId);
      return;
    }
    this.router.navigate(['/order-details', orderId]);
  }

  // ✅ Delete Order
  deleteOrder(orderId: number): void {
    if (confirm('Are you sure you want to delete this order?')) {
      this.orderService.deleteOrder(orderId).subscribe(
        () => {
          this.orders = this.orders.filter(order => order.orderId !== orderId);
          console.log(`Order ${orderId} deleted successfully`);
        },
        (error) => {
          console.error('Error deleting order:', error);
          alert('Failed to delete order. Please try again.');
        }
      );
    }
  }

  cancelOrder(orderId: number): void {
    if (confirm('Are you sure you want to cancel this order?')) {
      this.orderService.cancelOrder(orderId).subscribe(
        () => {
          console.log(`Order ${orderId} cancelled successfully.`);
          this.orders = this.orders.map(order =>
            order.orderId === orderId ? { ...order, status: 'CANCELLED' } : order
          );
        },
        (error) => {
          console.error('Error cancelling order:', error);
          alert('Failed to cancel the order. Please try again.');
        }
      );
    }
  }
  
}

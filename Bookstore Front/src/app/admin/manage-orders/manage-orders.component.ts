import { Component, OnInit } from '@angular/core';
import { OrderService } from '../../services/order.service';
import { Order } from '../../models/order.model';

@Component({
  selector: 'app-manage-orders',
  standalone: false,
  templateUrl: './manage-orders.component.html',
  styleUrls: ['./manage-orders.component.css']
})
export class ManageOrdersComponent implements OnInit {
  orders: Order[] = [];

  constructor(private orderService: OrderService) {}

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders() {
    this.orderService.getAllOrders().subscribe(data => {
      console.log("Fetched Orders:", data); // Check if orders exist
      if (data && data.length > 0) {
        console.log("First Order:", data[0]); // Check structure of the first order
      }
      this.orders = data;
    }, error => {
      console.error("Error fetching orders:", error);
    });
  }
  

  updateStatus(order: Order, newStatus: string) {
    this.orderService.updateOrderStatus(order.orderId, newStatus).subscribe(() => {
      order.status = newStatus; // Update UI after successful response
      alert('Successfully Updated ✅'); // Simple success message
    });
  }
  

  cancelOrder(orderId: number) {
    if (confirm("Are you sure you want to cancel this order?")) {
      this.orderService.cancelOrder(orderId).subscribe(() => {
        this.orders = this.orders.filter(order => order.orderId !== orderId); // Remove from list
      });
    }
  }
}
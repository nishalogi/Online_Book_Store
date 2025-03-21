import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-order-details',
  standalone: false,
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.css']
})
export class OrderDetailsComponent implements OnInit {
  orderId: number | null = null;
  order: any = null;
  errorMessage: string = '';
  isCanceling: boolean = false;
  orderCancelled: boolean = false; // ✅ Add this property

  constructor(private route: ActivatedRoute, private router: Router, private orderService: OrderService) {}

  ngOnInit(): void {
    this.orderId = Number(this.route.snapshot.paramMap.get('id'));

    if (!this.orderId || isNaN(this.orderId)) {
      this.errorMessage = 'Invalid Order ID.';
      return;
    }

    this.fetchOrderDetails();
  }

  fetchOrderDetails() {
    this.orderService.getOrderById(this.orderId!).subscribe(
      (data) => {
        this.order = data;
      },
      (error) => {
        this.errorMessage = 'Order not found.';
      }
    );
  }

  cancelOrder() {
    if (!this.order || this.order.status === 'CANCELLED') {
      alert('⚠️ This order has already been canceled.');
      return;
    }

    if (!confirm('Are you sure you want to cancel this order?')) {
      return;
    }

    this.isCanceling = true;
    this.orderService.cancelOrder(this.orderId!).subscribe(
      () => {
        alert('✅ Order canceled successfully!');
        this.order.status = 'CANCELLED'; 
        this.orderCancelled = true; // ✅ Set this to true when canceled
      },
      (error) => {
        alert('❌ Failed to cancel order. Try again.');
      }
    ).add(() => this.isCanceling = false);
  }

  confirmCancel() {
    if (confirm('Are you sure you want to cancel this order?')) {
      this.cancelOrder();
    }
  }

  goBack() {
    this.router.navigate(['/orders']);  // Change to your actual orders page route
}

  
}

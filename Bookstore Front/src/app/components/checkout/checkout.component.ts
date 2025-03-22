import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { AuthService } from '../../services/auth.service';
import { OrderService } from '../../services/order.service';

@Component({
  selector: 'app-checkout',
  standalone: false,
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent  implements OnInit {
  checkoutForm: FormGroup;
  cartItems: { book: any; quantity: number }[] = [];
  cartTotal: number = 0; //  Track total price

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private orderService: OrderService,
    private cartService: CartService,
    private router: Router
  ) {
    this.checkoutForm = this.fb.group({
      address: ['', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      paymentMethod: ['CASH_ON_DELIVERY', Validators.required] 
    });
    
  }

  ngOnInit() {
    this.loadCartItems();
  }

  loadCartItems() {
    this.cartService.getCartItems().subscribe({
      next: (items) => {
        this.cartItems = items || [];
        this.cartTotal = this.cartItems.reduce(
          (total, item) => total + (item.book?.price || 0) * item.quantity, 0
        );
      },
      error: (error) => {
        console.error('❌ Error loading cart items:', error);
      }
    });
  }
  
  placeOrder() {
    if (this.checkoutForm.invalid) {
      alert('❌ Please fill in all required fields.');
      return;
    }
  
    const userId = this.authService.getUserId();
    if (!userId) {
      alert('❌ Please login to place an order.');
      return;
    }
  
    if (this.cartItems.length === 0) {
      alert('❌ Your cart is empty! Please add books before checking out.');
      return;
    }
  
    const orderData = {
      bookIds: this.cartItems.map(item => item.book.id),  
      quantities: this.cartItems.map(item => item.quantity),  
      address: this.checkoutForm.value.address,
      phoneNumber: this.checkoutForm.value.phoneNumber,
      paymentMethod: this.checkoutForm.value.paymentMethod
    };
  
    this.orderService.placeOrder(userId, orderData).subscribe({
      next: () => {
        alert('✅ Order placed successfully!');
        this.cartService.clearCart(); 
        this.router.navigate(['/orders']); 
      },
      error: () => alert('❌ Error placing order. Please try again.')
    });
  }
}  

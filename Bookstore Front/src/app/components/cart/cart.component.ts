import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { AuthService } from '../../services/auth.service';
import { OrderService } from '../../services/order.service';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-cart',
  standalone:false,
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cartItems: any[] = [];
  totalPrice: number = 0;
  checkoutForm: FormGroup;
  isCheckoutMode = false;

  constructor(
    private cartService: CartService,
    private authService: AuthService,
    private orderService: OrderService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.checkoutForm = this.fb.group({
      address: ['', Validators.required],
      phoneNumber: ['', Validators.required],
      paymentMethod: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadCart();
  }

  loadCart() {
    this.cartService.getCartItems().subscribe(items => {
      this.cartItems = items;
      this.totalPrice = this.cartItems.reduce((total, item) => total + item.book.price * item.quantity, 0);
    });
  }

// Update quantity
updateQuantity(cartItemId: number, quantity: number) {
  if (quantity < 1) {
    alert('Quantity must be at least 1');
    return;
  }

  this.cartService.updateCartItem(cartItemId, quantity).subscribe({
    next: () => console.log('✅ Cart updated successfully'),
    error: (err) => console.error('❌ Error updating cart:', err)
  });
}


// Remove from cart
removeFromCart(cartItemId: number) {
  this.cartService.removeCartItem(cartItemId).subscribe({
    next: () => this.loadCart(),
    error: (error) => console.error('❌ Error removing item:', error)
  });
}// Add checkout function in cart.component.ts
checkout() {
  this.router.navigate(['/checkout']); // ✅ Redirects to Checkout Page
}





  clearCart() {
    this.cartService.clearCart().subscribe(() => {
      this.cartItems = [];
      this.totalPrice = 0;
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
      this.router.navigate(['/login']);
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
  
    console.log('📦 Checkout Order Data:', orderData); // Debugging log
  
    this.orderService.placeOrder(userId, orderData).subscribe({
      next: (response) => {
        console.log('✅ Order Response:', response); // Debugging log
        alert('✅ Order placed successfully!');
        this.clearCart();
        this.router.navigate(['/orders']);
      },
      error: (error) => {
        console.error('❌ Error placing order:', error);
        alert('❌ Error placing order. Please try again later.');
      }
    });
  }
}  
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CartService } from '../../services/cart.service';
import { BookService } from '../../services/book.service';
import { AuthService } from '../../services/auth.service';
import { OrderService } from '../../services/order.service';
import { ReviewService } from '../../services/review.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-book-details',
  standalone: false,
  templateUrl: './book-details.component.html',
  styleUrls: ['./book-details.component.css']
})
export class BookDetailsComponent implements OnInit {
  book: any = {};
  orderForm: FormGroup;
  isBuyingNow = false;
  orderSuccessMessage = ''; // Store success message
  showOrderSuccessPopup = false; // Controls popup visibility
  reviews: any[] = []; // Store fetched reviews
  reviewForm: FormGroup; // Form for submitting a review
  userId: number | null = null; // Store logged-in user ID

  paymentMethods = [
    { value: 'CASH_ON_DELIVERY', label: 'Cash on Delivery' },
    { value: 'ONLINE_PAYMENT', label: 'Online Payment' }
  ];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private cartService: CartService,
    private bookService: BookService,
    private authService: AuthService,
    private orderService: OrderService,
    private reviewService: ReviewService,
    private fb: FormBuilder
  ) {
    this.orderForm = this.fb.group({
      address: ['', Validators.required],
      phoneNumber: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      paymentMethod: ['', Validators.required]
    });

    // Initialize the review form
    this.reviewForm = this.fb.group({
      rating: ['', [Validators.required, Validators.min(1), Validators.max(5)]],
      comment: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.userId = this.authService.getUserId();

    this.route.paramMap.subscribe(params => {
      const bookId = params.get('id');
      if (bookId) {
        this.bookService.getBookById(Number(bookId)).subscribe({
          next: (data) => {
            this.book = data;
            this.fetchReviews(this.book.id); // Fetch reviews after getting book details
          },
          error: (err) => console.error('Error fetching book:', err)
        });
      }
    });
  }

  fetchReviews(bookId: number) {
    this.reviewService.getReviewsByBookId(bookId).subscribe({
      next: (reviews) => {
        this.reviews = reviews;
      },
      error: (err) => console.error('Error fetching reviews:', err)
    });
  }

  addReview() {
    if (!this.userId) {
      alert('❌ Please login to add a review.');
      return;
    }

    if (this.reviewForm.invalid) {
      alert('❌ Please provide a valid rating and comment.');
      return;
    }

    const reviewData = {
      bookId: this.book.id,
      userId: this.userId,
      rating: this.reviewForm.value.rating,
      comment: this.reviewForm.value.comment
    };

    this.reviewService.addReview(reviewData.bookId, reviewData.userId, reviewData.rating, reviewData.comment).subscribe({
      next: () => {
        alert('✅ Review added successfully!');
        this.reviewForm.reset();
        this.fetchReviews(this.book.id); // Refresh reviews after adding a new one
      },
      error: (err) => alert('❌ Failed to add review: ' + err.message)
    });
  }

  addToCart(book: any) {
    const userId = this.authService.getUserId();
    if (!userId) {
      alert('Please login to add books to the cart.');
      return;
    }

    const cartItem = { bookId: book.id, quantity: 1 };
    this.cartService.addToCart(cartItem).subscribe({
      next: () => alert('✅ Book added to cart!'),
      error: () => alert('❌ Failed to add book to cart.')
    });
  }

  buyNow() {
    this.isBuyingNow = true;
  }

  placeOrder() {
    if (this.orderForm.invalid) {
      alert('❌ Please fill in all fields.');
      return;
    }

    const userId = this.authService.getUserId();
    if (!userId) {
      alert('❌ Please login to place an order.');
      return;
    }

    if (!this.book || !this.book.id) {
      alert('❌ Book data is missing.');
      return;
    }

    const orderData = {
      bookIds: [this.book.id],
      quantities: [1],
      address: this.orderForm.value.address,
      phoneNumber: this.orderForm.value.phoneNumber,
      paymentMethod: this.orderForm.value.paymentMethod
    };

    this.orderService.placeOrder(userId, orderData).subscribe({
      next: () => {
        this.orderSuccessMessage = '✅ Order placed successfully!';
        this.showOrderSuccessPopup = true;
        this.orderForm.reset();
        this.isBuyingNow = false;

        // ✅ Hide success message after 2 seconds instead of redirecting
        setTimeout(() => {
          this.showOrderSuccessPopup = false;
        }, 2000);
      },
      error: () => alert('❌ Error placing order. Please try again.')
    });
  }
}

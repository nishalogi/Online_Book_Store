import { Component, OnInit } from '@angular/core';
import { CartService } from '../../services/cart.service';
import { ActivatedRoute, Router } from '@angular/router';
import { BookService } from '../../services/book.service';
import { Book } from '../../models/book.model';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-book-list',
  standalone: false,
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.css']
})
export class BookListComponent implements OnInit {
  books: Book[] = []; // Holds the list of books
  filteredBooks: Book[] = []; // Holds books after filtering
  searchQuery: string = '';
  isLoading: boolean = true;
  errorMessage: string = '';

  constructor(
    private bookService: BookService,
    private cartService: CartService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.fetchBooks();

    // Listen for search query updates dynamically
    this.route.queryParams.subscribe(params => {
      this.searchQuery = params['search'] || '';
      this.applySearch();
    });
  }

  fetchBooks(): void {
    this.bookService.getBooks().subscribe({
      next: (data: Book[]) => {
        this.books = data;
        this.filteredBooks = [...this.books];
        this.applySearch(); // ✅ Apply search after books are loaded
        this.isLoading = false;
      },
      error: (error) => {
        this.errorMessage = 'Failed to load books. Please try again later.';
        this.isLoading = false;
        console.error('Error fetching books:', error);
      }
    });
  }

  applySearch(): void {
    if (this.searchQuery.trim()) {
      this.filteredBooks = this.books.filter(book =>
        book.title.toLowerCase().includes(this.searchQuery.toLowerCase()) ||
        book.author.toLowerCase().includes(this.searchQuery.toLowerCase()) // ✅ Search by author too
      );
    } else {
      this.filteredBooks = [...this.books]; // Reset if search is empty
    }
  }

  addToCart(book: Book) {
    if (book.id === null) {
      console.error("Invalid book id: cannot add a book without a valid id to the cart.");
      return;
    }

    const userId = this.authService.getUserId();
    const token = localStorage.getItem('token');

    if (!token) {
      console.error('User is not authenticated.');
      return;
    }

    if (userId !== null) {
      const cartItem = { bookId: book.id, quantity: 1 };

      this.cartService.addToCart(cartItem).subscribe(
        (response) => {
          console.log('Book added to cart:', response);
        },
        (error) => {
          console.error('Error adding book to cart:', error);
        }
      );
    } else {
      console.error('User is not logged in.');
    }
  }
}

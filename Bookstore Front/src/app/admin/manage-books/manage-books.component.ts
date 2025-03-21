import { Component, OnInit } from '@angular/core';
import { BookService } from '../../services/book.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Book } from '../../models/book.model';

@Component({
  selector: 'app-manage-books',
  standalone: false,
  templateUrl: './manage-books.component.html',
  styleUrls: ['./manage-books.component.css']
})
export class ManageBooksComponent implements OnInit {
  books: Book[] = [];
  selectedBook: Book | null = null;
  showEditForm: boolean = false;
  apiUrl = 'http://localhost:8085/api/books';

  book: Book = {
    title: '',
    author: '',
    description: '',
    category:'',
    quantity: 0,
    price: 0,
    imageUrl: '',
    reviews: [],
    averageRating: 0,
    id: null
  };

  constructor(private bookService: BookService, private http: HttpClient) { }

  ngOnInit(): void {
    this.loadBooks();
  }

  // ✅ Load Books
  loadBooks() {
    this.bookService.getAllBooks().subscribe({
      next: (data) => this.books = data,
      error: (error) => {
        console.error("Error loading books:", error);
        alert("Failed to load books.");
      }
    });
  }

  // ✅ Open Edit Form
  editBook(book: Book) {
    this.selectedBook = { ...book };
    this.showEditForm = true;
  }


  // ✅ Update Book
  updateBook() {
    if (!this.selectedBook || !this.selectedBook.id) {
      console.error("No book selected for update.");
      return;
    }

    const token = localStorage.getItem('adminToken');
    if (!token) {
      alert("Unauthorized access. Please log in as an admin.");
      return;
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });

    this.http.put(`${this.apiUrl}/${this.selectedBook.id}`, this.selectedBook, { headers })
      .subscribe({
        next: () => {
          alert("Book updated successfully!");
          this.books = this.books.map(book =>
            book.id === this.selectedBook!.id ? { ...this.selectedBook! } : book
          );
          this.showEditForm = false;
        },
        error: (error) => {
          console.error('Error updating book', error);
          alert("Failed to update book.");
        }
      });
  }

  // ✅ Add New Book
  addNewBook() {
    const token = localStorage.getItem('adminToken'); // ✅ Ensure admin authentication
    if (!token) {
      console.error("No admin token found. Please log in as an admin.");
      alert("Unauthorized access. Please log in as an admin.");
      return;
    }
  
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,  // ✅ Include the token
      'Content-Type': 'application/json'
    });
  
    const newBook: Book = {
      id: null,  // ✅ Ensure ID is null
      title: this.book.title,
      author: this.book.author,
      description: this.book.description,
      category:this.book.category,
      quantity: this.book.quantity,
      price: this.book.price,
      imageUrl: this.book.imageUrl,
      reviews: [],
      averageRating: 0
    };
  
    this.http.post(`${this.apiUrl}/add`, newBook, { headers }).subscribe({
      next: () => {
        alert('Book added successfully!');
        this.book = { id: null, title: '', author: '', description: '',category:'', quantity: 0, price: 0, imageUrl: '', reviews: [], averageRating: 0 };
        this.loadBooks(); // Refresh list
      },
      error: (error) => {
        console.error('Error adding book:', error);
        alert('Failed to add book.');
      }
    });
  }

  deleteBook(bookId: number) {
    const confirmed = window.confirm('Are you sure you want to delete this book?');
  
    if (!confirmed) {
      return; // Stop if user cancels
    }
  
    const token = localStorage.getItem('token'); // Get JWT token
  
    if (!token) {
      console.error('User is not authenticated.');
      return;
    }
  
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  
    this.bookService.deleteBook(bookId, headers).subscribe(
      (response) => {
        console.log('Book deleted successfully:', response);
        this.loadBooks(); // Refresh book list after deletion
      },
      (error) => {
        console.error('Error deleting book:', error);
      }
    );
  }
  
}
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  standalone: false,
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'] // ✅ Fixed typo
})
export class HeaderComponent {
  searchQuery: string = '';

  constructor(private router: Router,public authService: AuthService) {}

  searchBooks() {
    if (this.searchQuery.trim()) {
      if (this.router.url.includes('/book-list')) {
        this.router.navigate([], { queryParams: { search: this.searchQuery } });
      } else {
        this.router.navigate(['/book-list'], { queryParams: { search: this.searchQuery } });
      }
    }
  }
  
  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  onLogout() {
    this.authService.logout();
    this.router.navigate(['/login']); 
}

isAdmin(): boolean {
  const role = localStorage.getItem('role')?.toUpperCase(); // Convert to uppercase
  console.log('User Role:', role);
  return role === 'ROLE_ADMIN'; // Now it always matches
}




}
import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    this.authService.login({ email: this.email, password: this.password }).subscribe({
      next: (response) => {
        if (response.token && response.userId) {
          // ✅ Store token & user data properly
          localStorage.setItem('token', response.token);
          localStorage.setItem('userId', response.userId.toString()); // Ensure userId is stored as a string
          localStorage.setItem('role', response.role);

          if (response.role === 'ROLE_ADMIN') {
            localStorage.setItem('adminToken', response.token);
          }

          alert('Login Successful!');
          this.errorMessage = ''; // ✅ Clear error message on success
          this.router.navigate(['/']); // ✅ Redirect to home page
        } else {
          this.errorMessage = 'Login failed. Invalid response from server.';
        }
      },
      error: (error) => {
        this.errorMessage = 'Invalid credentials. Please try again.';
        console.error('Login error:', error);
      }
    });
  }

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  onLogout() {
    this.authService.logout();
    window.location.reload(); // Reload to reflect logout
  }
}

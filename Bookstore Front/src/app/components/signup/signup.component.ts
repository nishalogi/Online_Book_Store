import { Component } from '@angular/core';
import { SignupService } from '../../services/signup.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-signup',
  standalone:false,
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent {
  user = { name: '', email: '', password: '' };
  errorMessage = '';

  constructor(private signupService: SignupService, private router: Router) {}

  onSignup() {
    this.signupService.signup(this.user).subscribe(
      response => {
        console.log('Signup successful', response);
        this.router.navigate(['/login']); // Redirect to login page after signup
      },
      error => {
        console.error('Signup failed', error);
        this.errorMessage = 'Signup failed. Please try again.';
      }
    );
  }
}


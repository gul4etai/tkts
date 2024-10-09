import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../shared/auth.service';
import { User } from '../shared/helper/user.class';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  standalone: true,
  imports: [FormsModule,CommonModule]
})
export class RegisterComponent {
  username: string = '';
  email: string = '';
  password: string = '';
  isAdmin: boolean = false;
  errorMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  register(): void {
    const newUser: User = {
      username: this.email,
      email: this.email,
      password: this.password,
      admin: this.isAdmin,
      bookings: []
    };

    this.authService.register(newUser).subscribe(
      (response) => {
        alert('Registration successful!');
        this.router.navigate(['/login']); // go to the login  after registration
      },
      (error) => {
        console.error('Registration failed:', error);
        this.errorMessage =
          error?.error?.message || 'An error occurred during registration';
      }
    );
  }
}

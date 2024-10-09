import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../shared/auth.service';
import {Location} from '@angular/common';

@Component({
  selector: 'app-auth',
  standalone: true,
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css',
  imports: [FormsModule, CommonModule]
})
export class AuthComponent {
  email = '';
  password = '';
  errorMessage = '';
  returnUrl: any;

  constructor(private authService: AuthService, private router: Router, private _location: Location, private route: ActivatedRoute ) {}


  ngOnInit(): void {
    // Get the return URL from route parameters (default to '/')
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
  }

  login(): void {
    this.authService.login(this.email, this.password).subscribe({
      next: (user) => {
        console.log('Login successful', user);
        this.router.navigateByUrl(this.returnUrl);
      },
      error: (err) => {
        console.error('Login failed:', err);
        this.errorMessage = 'Invalid credentials';
      }
    });
  }
  
}

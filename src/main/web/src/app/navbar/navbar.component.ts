import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from '../shared/auth.service';
import { User } from '../shared/helper/user.class';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [ RouterModule, CommonModule ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  currentUser: User | null = null;
  
  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    // get user details
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
    });
  }
  
  logout(): void {
    this.authService.logout();
  }

  isUserLoggedIn(): boolean {
    return this.currentUser !== null;
  }

  isUserAdmin(): boolean {
    return this.authService.isAdmin();
  }
  
  getDisplayName(username: string | undefined | null): string {
    if (!username) {
      return '';
    }
    // get name from email
    return username.split('@')[0];
  }
}


import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { User } from '../shared/helper/user.class';

@Component({
  selector: 'app-user-management',
  standalone: true,
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.css',
  imports: [CommonModule, FormsModule] 
})
export class UserManagementComponent implements OnInit {

  users: User[] = [];
  errorMessage: string = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.getAllUsers();
  }

  getAllUsers(): void {
    this.http.get<User[]>('http://localhost:8080/tkts/users') 
      .subscribe({
        next: (data) => {
          this.users = data;
        },
        error: (error) => {
          this.errorMessage = 'Error fetching users';
          console.error(error);
        }
      });
  }


  deleteUser(userId: number): void {
    if (confirm('Are you sure you want to delete this user?')) {
      this.http.delete(`http://localhost:8080/tkts/users/${userId}`) 
        .subscribe({
          next: () => {
            this.users = this.users.filter(user => user.id !== userId); 
            alert('User deleted successfully.');
          },
          error: (error) => {
            this.errorMessage = 'Error deleting user';
            console.error(error);
          }
        });
    }
  }
  isNotAdmin(user: User): boolean {
    return !user.admin;
  }

  makeAdmin(user: User): void {
    this.http.post(`http://localhost:8080/tkts/users/set-admin/${user.id}`, true) 
      .subscribe({
        next: () => {
          user.admin = true; 
          alert('User is now an admin.');
        },
        error: (error) => {
          this.errorMessage = 'Error updating user to admin';
          console.error(error);
        }
      });
  }
}

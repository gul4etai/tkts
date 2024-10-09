import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../shared/auth.service';
import { Theater } from '../shared/helper/theater.class';
import { MoviesService } from '../shared/movies.service';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-user-panel',
  standalone: true,
  imports: [ ReactiveFormsModule, CommonModule, RouterModule ],
  templateUrl: './user-panel.component.html',
  styleUrls: ['./user-panel.component.css'],
})
export class UserPanelComponent implements OnInit {
  userUpdateForm!: FormGroup;
  currentUser: any;
  theaters: Theater[] = [];

  constructor(private fb: FormBuilder, private authService: AuthService, private moviesService: MoviesService) {}

  ngOnInit(): void {
    //user update form
    this.userUpdateForm = this.fb.group({
      password: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
    });

    this.currentUser = this.authService.getCurrentUser();
    this.authService.updateUserInfo(this.currentUser.email);
    // get current user and fill form
    this.authService.currentUser$.subscribe(user => {
      this.currentUser = user;
      if (user) {
        this.userUpdateForm.patchValue({
          password: user.password,
          email: user.email,
        });
      }
    });

    //theater names for booking display
    this.moviesService.getTheaters().subscribe(theaters => {
      this.theaters = theaters;
    });
  }

  onUpdateUser(): void {
    if (this.userUpdateForm.valid) {
      this.authService.updateUser1(this.userUpdateForm.value).subscribe(updatedUser => {
        console.log('User updated successfully', updatedUser);
      });
    }
  }

  getTheaterName(theaterId: number): string {
    const theater = this.theaters.find(t => t.id === theaterId);
    return theater ? theater.name : 'Unknown Theater';
  }
}

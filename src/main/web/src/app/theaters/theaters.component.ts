// theater.component.ts

import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Theater } from '../shared/helper/theater.class'; 

@Component({
  selector: 'app-theater',
  standalone: true,
  templateUrl: './theaters.component.html',
  styleUrls: ['./theaters.component.css'],
  imports: [CommonModule, ReactiveFormsModule] 
})
export class TheaterComponent implements OnInit {
  theaters: Theater[] = []; 
  theaterForm!: FormGroup;   

  private apiUrl = 'http://localhost:8080/tkts/theaters'; 

  constructor(
    private fb: FormBuilder,
    private http: HttpClient 
  ) {}

  ngOnInit(): void {
    //form init
    this.theaterForm = this.fb.group({
      name: ['', Validators.required],
      rows: ['', [Validators.required, Validators.min(1)]],
      seats: ['', [Validators.required, Validators.min(1)]]
    });

    this.fetchTheaters();
  }

  fetchTheaters(): void {
    this.http.get<Theater[]>(this.apiUrl).subscribe(
      (theaters) => {
        this.theaters = theaters;
      },
      (error) => {
        console.error('Failed to fetch theaters:', error);
      }
    );
  }

  addTheater(): void {
    if (this.theaterForm.valid) {
      const newTheater: Theater = this.theaterForm.value;
      this.http.post<Theater>(this.apiUrl, newTheater).subscribe(
        (theater) => {
          this.theaters.push(theater); 
          this.theaterForm.reset(); 
        },
        (error) => {
          console.error('Failed to add theater:', error);
        }
      );
    }
  }

  deleteTheater(theaterId: number): void {
    this.http.delete<void>(`${this.apiUrl}/${theaterId}`).subscribe(
      () => {
        // remove theater from list
        this.theaters = this.theaters.filter(theater => theater.id !== theaterId);
      },
      (error) => {
        console.error('Failed to delete theater:', error);
      }
    );
  }
}

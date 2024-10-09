import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; // For common directives
import { FormsModule } from '@angular/forms'; // For ngModel
import { HttpClient } from '@angular/common/http'; // For HTTP requests
import { map } from 'rxjs/operators'; // To transform the response data
import { Movie } from '../shared/helper/movie.class'; // Import the Movie class
import { User } from '../shared/helper/user.class';  // Import the User class

@Component({
  selector: 'app-reports',
  standalone: true,
  templateUrl: './reports.component.html',
  styleUrls: ['./reports.component.css'],
  imports: [CommonModule, FormsModule] 
})
export class ReportsComponent {

  movieTitles: string[] = [];  
  usernames: string[] = [];  
  errorMessage: string = '';  
  startDate: string = '';  
  endDate: string = '';    

  constructor(private http: HttpClient) {}

  fetchMostWatchedMovieTitles(): void {
    this.clearPreviousData();

    if (!this.startDate || !this.endDate) {
      this.errorMessage = 'Please select both start and end dates.';
      return;
    }

    this.http.get<Movie[]>(`http://localhost:8080/tkts/movies/most-booked/${this.startDate}/${this.endDate}`)
      .pipe(
        map(movies => movies.map(movie => movie.title))  // get only titles
      )
      .subscribe({
        next: (titles: string[]) => {
          this.movieTitles = titles;  
          this.errorMessage = '';
        },
        error: (error) => {
          this.errorMessage = 'Error fetching movie titles';
          console.error(error);
        }
      });
  }

  fetchLeastWatchedMovieTitles(): void {
    this.clearPreviousData();

    if (!this.startDate || !this.endDate) {
      this.errorMessage = 'Please select both start and end dates.';
      return;
    }

    this.http.get<Movie[]>(`http://localhost:8080/tkts/movies/least-booked/${this.startDate}/${this.endDate}`)
      .pipe(
        map(movies => movies.map(movie => movie.title))  
      )
      .subscribe({
        next: (titles: string[]) => {
          this.movieTitles = titles;  
          this.errorMessage = '';
        },
        error: (error) => {
          this.errorMessage = 'Error fetching movie titles';
          console.error(error);
        }
      });
  }

  fetchLeastActiveUsernames(): void {
    this.clearPreviousData();

    if (!this.startDate || !this.endDate) {
      this.errorMessage = 'Please select both start and end dates.';
      return;
    }

    this.http.get<User[]>(`http://localhost:8080/tkts/users/least-active/${this.startDate}/${this.endDate}`)
      .pipe(
        map(users => users.map(user => user.username))  // get only usernames
      )
      .subscribe({
        next: (usernames: string[]) => {
          this.usernames = usernames;  
          this.errorMessage = '';
        },
        error: (error) => {
          this.errorMessage = 'Error fetching usernames';
          console.error(error);
        }
      });
  }

  fetchMostActiveUsernames(): void {
    this.clearPreviousData();

    if (!this.startDate || !this.endDate) {
      this.errorMessage = 'Please select both start and end dates.';
      return;
    }

    this.http.get<User[]>(`http://localhost:8080/tkts/users/most-active/${this.startDate}/${this.endDate}`)
      .pipe(
        map(users => users.map(user => user.username))  
      )
      .subscribe({
        next: (usernames: string[]) => {
          this.usernames = usernames;  
          this.errorMessage = '';
        },
        error: (error) => {
          this.errorMessage = 'Error fetching usernames';
          console.error(error);
        }
      });
  }

  private clearPreviousData(): void {
    this.movieTitles = [];
    this.usernames = [];
    this.errorMessage = '';
  }
}

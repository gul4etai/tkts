import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { MoviesService } from '../shared/movies.service';
import { Observable } from 'rxjs';
import { Movie } from '../shared/helper/movie.class';
import { CommonModule } from '@angular/common';
import { Location } from '@angular/common';

@Component({
  selector: 'app-admin-panel',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.css']
})
export class AdminPanelComponent {
  movies$: Observable<Movie[]>;

  constructor(private router: Router, private moviesService: MoviesService, private _location: Location) {
    this.movies$ = this.moviesService.getMovies();
  }

  editMovie(movieId: number): void {
    this.router.navigate(['admin/edit-movie/' + movieId]);  
  }

  deleteMovie(movieId: number): void {
    if (confirm('Are you sure you want to delete this movie?')) {
      this.moviesService.deleteMovie(movieId);  
      // refresh the movie list
      this.movies$ = this.moviesService.getMovies();
    }
  }

  goBack(): void {
    this._location.back();
  }
}

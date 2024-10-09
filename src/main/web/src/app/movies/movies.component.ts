import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MoviesService } from '../shared/movies.service';
import { Movie } from '../shared/helper/movie.class';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-movies',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './movies.component.html',
  styleUrls: ['./movies.component.css'],
})
export class MoviesComponent implements OnInit {
  movies$: Observable<Movie[]>;  

  constructor(private moviesService: MoviesService, private router: Router) {
    this.movies$ = this.moviesService.getMovies();  
  }

  ngOnInit(): void {
  }

  goToMovieDetails(id: number): void {
    this.router.navigate(['/movie/' + id]);
  }
}

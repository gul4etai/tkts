import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { MoviesService } from '../shared/movies.service';
import { MoviesComponent } from "../movies/movies.component";
import { FormsModule } from '@angular/forms';
import { Genre } from '../shared/helper/genre.enum';

@Component({
  selector: 'app-home-page',
  standalone: true,
  imports: [CommonModule, RouterModule, MoviesComponent, FormsModule],
  templateUrl: './home-page.component.html',
  styleUrl: './home-page.component.css'
})
export class HomePageComponent {
  genres: string[] = Object.values(Genre);

  filterName = ''; 
  selectedGenre = ''; 

  private httpClient = inject(HttpClient);

  constructor(private moviesService: MoviesService) {
  }

  ngOnInit(): void {
    this.resetFilters();
  }

  ngOnDestroy(): void {
    this.resetFilters();
  }

  filterMovies(): void {
    this.moviesService.filterMoviesByTitle(this.filterName);
    this.moviesService.filterMovies('genre', this.selectedGenre);
  }

  onFilterChange(): void {
    this.filterMovies();
  }

  resetFilters(): void {
    this.filterName = '';
    this.selectedGenre = '';
    this.filterMovies();
  }
}

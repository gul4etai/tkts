import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Genre } from '../../../shared/helper/genre.enum';
import { Movie } from '../../../shared/helper/movie.class';
import { Screening } from '../../../shared/helper/screening.class';
import { MoviesService } from '../../../shared/movies.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Theater } from '../../../shared/helper/theater.class';
import { Location } from '@angular/common';

@Component({
  selector: 'app-edit-movie',
  imports: [ CommonModule, FormsModule],
  standalone: true,
  templateUrl: './edit-movie.component.html',
  styleUrls: ['./edit-movie.component.css']
})
export class EditMovieComponent implements OnInit {
  movie: Movie = new Movie(0, '', '', '', 0, Genre.Action, []);  
  genres = Object.values(Genre);  
  newScreening: Screening = new Screening(0, '', '', []);
  selectedTheaterId: number | undefined;
  theaters!: Theater[];
  screeningsForSelectedTheater: Screening[] | undefined;
  isNewMovie: boolean = false;  

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private moviesService: MoviesService,
    private _location: Location, 
  ) {}

  ngOnInit(): void {
    const movieId = this.route.snapshot.paramMap.get('id');
    
    if (movieId) {
      // existing movie
      this.moviesService.getMovieById(Number(movieId)).subscribe(movie => {
        if (movie) {
          this.movie = movie;
        }
      });
    } else {
      this.isNewMovie = true;
      this.movie = new Movie(0, '', '', '', 0, Genre.Action, []);  
    }

    this.moviesService.getTheaters().subscribe(theaters => {
      this.theaters = theaters;
    });
  }

  onTheaterSelect(): void {
    if (this.selectedTheaterId && this.movie?.screenings) {
      // filter screenings by theater
      this.screeningsForSelectedTheater = this.movie.screenings.filter(s => s.theaterId == this.selectedTheaterId);
    } else {
      this.screeningsForSelectedTheater = [];  // clear screenings if no theater 
    }
  }

  addScreening(): void {
    if (this.movie && this.newScreening.date && this.newScreening.time.length > 0 && this.selectedTheaterId) {
 
      const newScreening = {
        id: this.movie.screenings.length + 1,  
        theaterId: this.selectedTheaterId,
        date: this.newScreening.date,
        time: this.newScreening.time,
        occupiedSeats: []  
      };
  
      this.movie.screenings.push(newScreening);
      // add screening to existing screenings 
      this.screeningsForSelectedTheater?.push(newScreening);
      // reset the form after adding screenings
      this.newScreening = new Screening(this.selectedTheaterId, '', '', []);
    }
  }
  
  formatTime(input: any): string {
    if (typeof input !== 'string') {
      return input;  
    }

    input = input.trim().toLowerCase();
    const timePattern = /^(0?[1-9]|1[0-2]):[0-5][0-9]\s?(am|pm)$/;

    if (timePattern.test(input)) {
      return input.toUpperCase();  
    }

    const numericPattern = /^(\d{1,2})([ap]m)?$/;
    if (numericPattern.test(input)) {
      const match = input.match(numericPattern);
      let hours = parseInt(match![1], 10);
      const period = match![2] || (hours < 12 ? 'am' : 'pm');

      const formattedHours = hours < 10 ? `0${hours}` : hours.toString();
      return `${formattedHours}:00 ${period.toUpperCase()}`;
    }

    return input; 
  }

  getSelectedTheaterName(): string | undefined {
    const theater = this.theaters.find(theater => theater.id === this.selectedTheaterId);
    return theater ? theater.name : undefined;
  }  

  saveChanges(): void {
    if (this.isNewMovie) {
      this.movie.id = this.generateNewMovieId();
      this.moviesService.addOrUpdateMovie(this.movie);  
    } else {
      this.moviesService.addOrUpdateMovie(this.movie);
    }
    this.router.navigate(['/admin']);  // redirect to admin panel 
  }

  generateNewMovieId(): number {
    const maxId = Math.max(...this.moviesService.getMoviesSync().map(m => m.id));
    return maxId + 1;  
  }

  removeScreening(screening: Screening): void {
    if (this.movie) {
      this.movie.screenings = this.movie.screenings.filter(s => s !== screening);
    }
  }

  goBack(): void {
    this._location.back();
  }
}

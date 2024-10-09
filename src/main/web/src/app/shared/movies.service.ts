import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { map, switchMap } from 'rxjs/operators';
import { Movie } from './helper/movie.class';
import { Screening } from './helper/screening.class';
import { Seat } from './helper/seat.class';
import { Theater } from './helper/theater.class';

@Injectable({
  providedIn: 'root',
})
export class MoviesService {
  private moviesSubject = new BehaviorSubject<Movie[]>([]);
  movies$ = this.moviesSubject.asObservable();
  private moviesData: Movie[] = [];
  private filteredMovies!: Movie[];

  constructor(private http: HttpClient) {
    this.loadMovies(); 
  }

  loadMovies(): void {
    this.http.get<Movie[]>('http://localhost:8080/tkts/movies').subscribe(movies => {
      this.moviesData = movies; 
      this.moviesSubject.next(movies);
    });
  }

  getMovies(): Observable<Movie[]> {
    return this.movies$;
  }

  getTheaters(): Observable<Theater[]> {
    return this.http.get<Theater[]>('http://localhost:8080/tkts/theaters');
  }

  generateSeatMap(rows: number, seatsPerRow: number, occupiedSeats: number[][]): Seat[][] {
    const seatMap = [];
  
    for (let row = 1; row <= rows; row++) {
      const rowSeats = [];
      for (let seat = 1; seat <= seatsPerRow; seat++) {
        const isOccupied = occupiedSeats.some(seatPair => seatPair[0] === row && seatPair[1] === seat);
        rowSeats.push(new Seat(row, seat, false, !isOccupied));
      }
      seatMap.push(rowSeats);
    }
  
    return seatMap;
  }
  

  getSeats(movieId: number, theaterId: number, date: string, time: string): Observable<Seat[][]> {
    return this.getMovieById(movieId).pipe(
      map(movie => {
        const screening = movie?.screenings.find(s => 
          s.theaterId == theaterId && 
          s.date == date && 
          s.time == time
        );
  
        const occupiedSeats = screening?.occupiedSeats || [];
        
        return this.getTheaterById(theaterId).pipe(
          map(theater => {
            const rows = theater?.rows ?? 7;
            const seatsPerRow = theater?.seats ?? 8;
    
            const seatMap = this.generateSeatMap(rows, seatsPerRow, occupiedSeats);
            return seatMap;
          })
        );
      }),
      switchMap(seatMap$ => seatMap$)
    );
  }
  
  filterMoviesByTitle(title: string): void {
    this.filteredMovies = this.moviesData.filter(movie =>
      movie.title.toLowerCase().includes(title.toLowerCase())
    );
    this.moviesSubject.next(this.filteredMovies);
  }

  filterMovies<K extends keyof Movie>(key: K, value: string | number): void {
    const filteredMovies = this.filteredMovies.filter(movie =>
      movie[key].toString().toLowerCase().includes(value.toString().toLowerCase())
    );
    this.moviesSubject.next(filteredMovies);
  }

  updateMovies(newMovies: Movie[]): void {
    this.moviesData = newMovies;
    this.moviesSubject.next(this.moviesData); 
  }

  addOrUpdateMovie(movie: Movie): void {
    const index = this.moviesData.findIndex(m => m.id === movie.id);

    if (index !== -1) {
      // existing movie update
      this.http.put(`http://localhost:8080/tkts/movies/${movie.id}`, movie).subscribe(
        updatedMovie => {
          console.log(`## Updated movie`, updatedMovie);
          this.moviesData[index] = movie;
          this.moviesSubject.next([...this.moviesData]);
        },
        error => {
          console.error('Failed to update movie:', error);
        }
      );
    } else {
      // new movie add
      this.http.post<Movie>('http://localhost:8080/tkts/movies', movie).subscribe(
        newMovie => {
          console.log('### Added new movie:', newMovie);
          this.moviesData.push(newMovie);
          this.moviesSubject.next([...this.moviesData]);
        },
        error => {
          console.error('Failed to add movie:', error);
        }
      );
    }
  }

  getMovieById(id: number): Observable<Movie | undefined> {
    return this.movies$.pipe(
      map(movies => movies.find(movie => movie.id == id))
    );
  }

  getTheaterById(id: number): Observable<Theater | undefined> {
    return this.getTheaters().pipe(
      map(theaters => theaters.find(t => t.id == id))
    );
  }

  getScreeningsForMovie(movieId: number, theaterId: number): Screening[] {
    const screenings = this.moviesData.filter(movie => movie.id === movieId)[0].screenings;
    return screenings.filter(s => s.theaterId === theaterId);
  }

  getMoviesSync(): Movie[] {
    return this.moviesData;
  }

  deleteMovie(movieId: number): void {
    this.http.delete(`http://localhost:8080/tkts/movies/` + movieId).subscribe(
      (response) => {
        console.log(`Response of dfelete:` + response);
        this.moviesData = this.moviesData.filter(movie => movie.id !== movieId);
        this.moviesSubject.next(this.moviesData);
      },
      error => {
        console.error('Failed to delete movie:', error);
      }
    );
  }
}

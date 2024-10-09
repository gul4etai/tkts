import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MoviesService } from '../../shared/movies.service';
import { Movie } from '../../shared/helper/movie.class';
import { Theater } from '../../shared/helper/theater.class';
import { Observable, of } from 'rxjs';
import { Location } from '@angular/common';
import { CheckoutService } from '../../shared/checkout.service';
import { Seat } from '../../shared/helper/seat.class';
import { Screening } from '../../shared/helper/screening.class';

@Component({
  selector: 'app-movie',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './movie.component.html',
  styleUrls: ['./movie.component.css'],
})
export class MovieDetailsComponent implements OnInit {
  movie: Movie | undefined;
  theaters$: Observable<Theater[]>;  
  screenings$: Observable<Screening[]> = new Observable(); 
  seatMap$: Observable<Seat[][]> = new Observable();  
  availableTimes: string[] = [];  // by selected date
  uniqueDates: string[] = []; 
  selectedTheaterId: number | undefined = undefined;
  selectedDate: string | undefined = undefined;
  selectedTime: string | undefined = undefined;
  selectedSeats: { row: number; seat: number }[] = []; 
  theaters!: Theater[];
  selectedTheater!: Theater | undefined;

  constructor(private route: ActivatedRoute, 
              private moviesService: MoviesService, 
              private _location: Location, 
              private router: Router, 
              private checkoutService: CheckoutService ) {
    this.theaters$ = this.moviesService.getTheaters();  
  }

  ngOnInit(): void {
    const movieId = +this.route.snapshot.paramMap.get('id')!;
    this.moviesService.getMovieById(movieId).subscribe(movie => {
      this.movie = movie;
    });
    
    this.moviesService.getTheaters().subscribe((data: Theater[]) => {
      this.theaters = data;  // put theaters in the local variable
      console.log(this.theaters); 
    });
  }

  onTheaterSelect(): void {
    if (this.selectedTheaterId !== undefined) {
      const filteredScreenings = this.movie?.screenings.filter(s => s.theaterId == this.selectedTheaterId) || [];
      this.uniqueDates = [...new Set(filteredScreenings.map(screening => screening.date))];
    
      this.screenings$ = of(filteredScreenings);
      this.selectedTheater = this.theaters.find(t => t.id == this.selectedTheaterId);
      this.screenings$.subscribe(screenings => {
        this.selectedDate = undefined;  // Reset data if new theater is selected
        this.availableTimes = [];  
        this.selectedTime = undefined;  
        this.selectedSeats = [];  
        this.seatMap$ = new Observable();
      });
    }
  }

  onDateSelect(): void {
    if (this.screenings$ && this.selectedDate) {
      this.screenings$.subscribe(screenings => {
        //filter screenings by date
        const screeningsForSelectedDate = screenings.filter(screening => screening.date === this.selectedDate);
        // get times
        this.availableTimes = screeningsForSelectedDate.map(screening => screening.time);
        // reset time
        this.selectedTime = '';  
      });
    }
  }

  loadSeats(): void {
    if (this.selectedTheaterId && this.selectedDate && this.selectedTime && this.movie) {
      this.seatMap$ = this.moviesService.getSeats(this.movie.id, this.selectedTheaterId, this.selectedDate, this.selectedTime);
      this.selectedSeats = [];  // Reset selected seats 
    }
  }

  isSelected(rowIndex: number, seatIndex: number): boolean {
    return this.selectedSeats.some(seat => seat.row === rowIndex && seat.seat === seatIndex);
  }


  selectSeat(row: number, seat: number): void {
    const seatIndexInSelection = this.selectedSeats.findIndex(ticket => ticket.row === row && ticket.seat === seat);
    if (seatIndexInSelection > -1) {
      this.selectedSeats.splice(seatIndexInSelection, 1);  // remove already selected seat
    } else {
      this.selectedSeats.push({ row: row, seat: seat});  // add selected seats
    }
  }

  goBack(): void {
    this._location.back();
  }

  goToCheckout(): void {
    // save data to the service
    console.log("Selected seats:", this.selectedSeats);
    this.checkoutService.setSelectedSeats(this.selectedSeats);
    this.checkoutService.setMovie(this.movie!);
    this.checkoutService.setTheater(this.selectedTheater!);
    this.checkoutService.setDate(this.selectedDate!);
    this.checkoutService.setTime(this.selectedTime!);

    this.router.navigate(['/checkout']);
  }
}
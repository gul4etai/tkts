import { Injectable } from '@angular/core';
import { Theater } from './helper/theater.class';
import { Movie } from './helper/movie.class';
import { Reservation } from './helper/reservation.class';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CheckoutService {
  private selectedSeats: { row: number, seat: number }[] = [];
  private movie: any = null;  
  private theater: any = null;  
  private date: string = '';  
  private time: string = '';  

  constructor(private http: HttpClient){

  }
  setSelectedSeats(seats: { row: number, seat: number }[]): void {
    this.selectedSeats = seats;
  }

  setMovie(movie: Movie): void {
    this.movie = movie;
  }

  setTheater(theater: Theater): void {
    this.theater = theater;
  }

  setDate(date: string): void {
    this.date = date;
  }

  setTime(time: string): void {
    this.time = time;
  }

  getSelectedSeats(): { row: number, seat: number }[] {
    return this.selectedSeats;
  }

  getMovie(): any {
    return this.movie;
  }

  getTheater(): any {
    return this.theater;
  }

  getDate(): string {
    return this.date;
  }

  getTime(): string {
    return this.time;
  }

  clearCheckoutData(): void {
    this.selectedSeats = [];
    this.movie = null;
    this.theater = null;
    this.date = '';
    this.time = '';
  }

  sendReservation(reservation: Reservation): Observable<any> {
    console.log("### Reserved: ", reservation);
    
    return this.http.post<any>('http://localhost:8080/tkts/bookings', reservation);
  }
}

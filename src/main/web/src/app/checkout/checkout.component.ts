import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Movie } from '../shared/helper/movie.class';
import { CommonModule } from '@angular/common';
import { Location } from '@angular/common';
import { Theater } from '../shared/helper/theater.class';
import { CheckoutService } from '../shared/checkout.service';
import { Reservation } from '../shared/helper/reservation.class';
import { Router } from '@angular/router';
import * as bootstrap from 'bootstrap';
import { MoviesService } from '../shared/movies.service';


@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [ CommonModule],
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit {
  reservationNumber!: string;  // To store the success number
  @ViewChild('successModal', { static: false }) successModal!: ElementRef;
  @ViewChild('errorModal', { static: false }) errorModal!: ElementRef;

  selectedSeats: { row: number, seat: number }[] = [];  // Seats passed from MovieDetailsComponent
  movie!: Movie;
  moviePrice!: number;
  theater!: Theater;
  date!: string;
  time!: string;
  modalInstance: any;
  errorModalInstance: any;  // Instance for error modal
  errorMessage: string = '';  // Error message to display in modal

  constructor(private router: Router,
              private _location: Location, 
              private moviesService:MoviesService,
              private checkoutService: CheckoutService) {}

  ngOnInit(): void {
    this.selectedSeats = this.checkoutService.getSelectedSeats();
    this.movie = this.checkoutService.getMovie();
    this.theater = this.checkoutService.getTheater();
    this.date = this.checkoutService.getDate();
    this.time = this.checkoutService.getTime();
    
    if(!this.movie || !this.theater) {
      this.checkoutService.clearCheckoutData();
      this.router.navigate(['/']);
    }
  }

  ngAfterViewInit(): void {
    if (this.successModal && this.successModal.nativeElement) {
      // Initialize the success modal using Bootstrap's JS API
      this.modalInstance = new bootstrap.Modal(this.successModal.nativeElement);
    } else {
      console.error('Success Modal element not found');
    }

    if (this.errorModal && this.errorModal.nativeElement) {
      // Initialize the error modal using Bootstrap's JS API
      this.errorModalInstance = new bootstrap.Modal(this.errorModal.nativeElement);
    } else {
      console.error('Error Modal element not found');
    }
  }


  goBack(): void {
    this._location.back();
  }

  reserveTickets(): void {
    const reservation = new Reservation (this.movie.id, this.theater.id, this.date, this.time, this.selectedSeats);
    this.checkoutService.sendReservation(reservation).subscribe(
      response => {
        this.reservationNumber = response?.id;
         // Clear the checkout data after a successful reservation
      this.checkoutService.clearCheckoutData();
        this.showSuccessModal();
        this.moviesService.loadMovies();
              // Wait for the modal to be shown and then navigate after a delay
      setTimeout(() => {
        this.modalInstance.hide(); // Hide the modal
        this.router.navigate(['/']); // Navigate to the home page
      }, 2000); // Adjust the delay time as needed (2 seconds)
      },
      error => {
         // Handle different error codes and set the error message
         if (error.status === 409) {
          this.errorMessage = 'Someone beat you to it! Some of the selected seats are already booked. Please choose different seats.';
        } else {
          this.errorMessage = 'An unexpected error occurred. Please try again later.';
        }
        this.showErrorModal();
      }
    );
  }

  showSuccessModal(): void {
    this.modalInstance.show();
  }

  showErrorModal(): void {
    this.errorModalInstance.show();
  }

  closeErrorModal(): void {
    this.errorModalInstance.hide();
    this.router.navigate(['/movies' + this.movie.id]);  
  } 

  redirectToHome(): void {
    this.modalInstance.hide();
    this.router.navigate(['/']);
  }
}

import { Screening } from './screening.class';
import { User } from './user.class';
import { Ticket } from './ticket.class';

export class Booking {
  id: number;
  movieTitle: string;
  bookingTime: Date;
  screening: Screening;
  user: User;
  tickets: Ticket[];

  constructor(
    id: number,
    movieTitle: string,
    bookingTime: Date,
    screening: Screening,
    user: User,
    tickets: Ticket[]
  ) {
    this.id = id;
    this.movieTitle = movieTitle;
    this.bookingTime = bookingTime;
    this.screening = screening;
    this.user = user;
    this.tickets = tickets;
  }
}

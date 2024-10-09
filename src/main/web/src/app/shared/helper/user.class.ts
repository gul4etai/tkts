import { Booking } from './booking.class'; 

export class User {
  id?: number;
  password: string;
  email: string;
  username: string;
  admin: boolean;
  bookings: Booking[];

  constructor(
    id: number,
    password: string,
    email: string,
    username: string,
    admin: boolean,
    bookings: Booking[]
  ) {
    this.id = id;
    this.password = password;
    this.email = email;
    this.username = username;
    this.admin = admin;
    this.bookings = bookings;
  }
}

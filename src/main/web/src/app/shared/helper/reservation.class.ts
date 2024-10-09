import { Seat } from "./seat.class";

export class Reservation {
    movieId: number;
    theaterId: number;
    date: string;
    time: string;
    seats: { row: number, seat: number }[];

    constructor(movieId:number, theaterId: number, date: string, time: string, seats: { row: number, seat: number }[]) {
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.date = date;
        this.time = time;
        this.seats = seats;
    }
}
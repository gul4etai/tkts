export class Ticket {
    id: number;
    screeningId: number;
    bookingId: number;
    seatId: number;
    status: number;
  
    constructor(
      id: number,
      screeningId: number,
      bookingId: number,
      seatId: number,
      status: number
    ) {
      this.id = id;
      this.screeningId = screeningId;
      this.bookingId = bookingId;
      this.seatId = seatId;
      this.status = status;
    }
  }
  
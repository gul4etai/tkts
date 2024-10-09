export class Screening {
    theaterId!: number;
    date: string;
    time: string;
    occupiedSeats: number[][];
  
    constructor(theaterId: number, date: string, times: string, occupiedSeats: number[][]) {
      this.theaterId = theaterId;
      this.date = date;
      this.time = times;
      this.occupiedSeats = occupiedSeats;
    }
  }
  
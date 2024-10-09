export class Theater {
    id: number;
    name: string;
    rows: number;
    seats: number;
    screenings?: { movieId: number; date: string; times: string[] }[];
  
    constructor(id: number, name: string, rows:number, seats:number, screenings: { movieId: number; date: string; times: string[] }[]) {
      this.id = id;
      this.name = name;
      this.rows = rows;
      this.seats = seats;
      this.screenings = screenings;
    }
  }
  
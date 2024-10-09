export class Seat {
    row: number;
    seatNumber: number;
    isSelected: boolean;
    available: boolean;
  
    constructor(row: number, seatNumber: number, isSelected = false, available: boolean) {
      this.row = row;
      this.seatNumber = seatNumber;
      this.isSelected = isSelected;
      this.available = available;
    }
  }
  
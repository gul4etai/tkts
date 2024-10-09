import { Genre } from "./genre.enum";
import { Screening } from "./screening.class";

export class Movie {
    id: number;
    title: string;
    description: string;
    imgURL: string;
    price: number;
    genre: Genre;
    screenings: Screening[] = []; 
  
    constructor(id: number, title: string, description: string, imgURL: string, price: number, genre: Genre, screenings: Screening[]) {
      this.id = id;
      this.title = title;
      this.description = description;
      this.imgURL = imgURL;
      this.price = price;
      this.genre = genre;
      this.screenings = screenings;
    }
  }
  
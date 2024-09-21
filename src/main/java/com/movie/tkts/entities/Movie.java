package com.movie.tkts.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;
    private String title;
    private double price;
    private String genre;
    private int duration;
    private String imgURL;
    private String description;


    @OneToMany(mappedBy = "movie")
    private List<Screening> screenings;
}

package com.movie.tkts.entities;

import com.movie.tkts.dto.ScreeningDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   // @Column(name = "movie_id")
    private Long id;
    private String title;
    private double price;
    private String genre;
    private int duration;
    @Column(name = "img_url")
    private String imgURL;
    private String description;


//    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
  // private List<Screening> screenings;
}

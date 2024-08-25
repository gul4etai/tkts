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
    private String genre;
    private int ageGroup;
    private int duration;
    private String imgURL;
    private String description;

//    // One Movie can have many Screenings
//    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Screening> screenings;

}

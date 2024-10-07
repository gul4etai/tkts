package com.movie.tkts.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movie.tkts.dto.ScreeningDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private double price;
    private String genre;
    private int duration;
    @Column(name = "img_url")
    private String imgURL;
    private String description;

  @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Screening> screenings;
}

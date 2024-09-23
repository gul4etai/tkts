package com.movie.tkts.entities;

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
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "theater_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    private int rows;
    private int seatsInRow;


    //remove?
//    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Screening> screenings  = new ArrayList<>();

    @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Seat> seats  = new ArrayList<>();
}

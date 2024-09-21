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
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theaterId;

    @Column(nullable = false, unique = true)
    private String name;
    private int rows;
    private int seatsInRow;

    @OneToMany(mappedBy = "theater")
    private List<Screening> screenings;

    @OneToMany(mappedBy = "theater")
    private List<Seat> seats;
}

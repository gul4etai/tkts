package com.movie.tkts.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "screening_id")
    private Long id;
    private LocalDate date; //key?
    private LocalTime time;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "theater_id")
    private Theater theater;

/*    @ManyToMany
    @JoinTable(
            name = "screening_booked_seats",
            joinColumns = @JoinColumn(name = "screening_id"),
            inverseJoinColumns = @JoinColumn(name = "seat_id")
    )
    private List<Seat> bookedSeats = new ArrayList<>();*/

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();
}

package com.movie.tkts.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Screening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "screening_id")
    private Long id;
    private LocalDate date;
    private LocalTime time;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    @JsonIgnore
    private Movie movie;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "theater_id")
    @JsonIgnore
    private Theater theater;

    @OneToMany(mappedBy = "screening")  //cascade deletion is handled in booking
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "screening", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;


}

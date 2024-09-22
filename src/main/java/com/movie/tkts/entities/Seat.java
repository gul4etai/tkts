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
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "seat_id")
    private Long id;

    @Column(nullable = false)
    int seatNum;
    @Column(nullable = false)
    int rowNum;

    @ManyToOne
    @JoinColumn(name = "theater_id", nullable = false)
    private Theater theater;

  /*  @OneToMany(mappedBy = "seat")
    private List<Ticket> tickets;*/

}

package com.movie.tkts.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JsonIgnore
    private Theater theater;

  /*  @OneToMany(mappedBy = "seat")
    private List<Ticket> tickets;*/

}

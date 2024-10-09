package com.movie.tkts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.movie.tkts.entities.Screening;
import com.movie.tkts.entities.Seat;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TheaterDto {
    private Long id;
    private String name;
    private int rows;
    @JsonProperty ("seats")
    private int seatsInRow;
//    private List<ScreeningDto> screenings;
 // private List<SeatDto> seats;
}

package com.movie.tkts.dto;

import com.movie.tkts.entities.Movie;
import com.movie.tkts.entities.Seat;
import com.movie.tkts.entities.Theater;
import com.movie.tkts.entities.Ticket;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScreeningDto {

    private Long id;
    private LocalDate date;
    private LocalTime time;
    private MovieDto movie;
    private TheaterDto theater;
    private List<SeatDto> seats;
}

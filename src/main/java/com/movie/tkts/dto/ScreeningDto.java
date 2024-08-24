package com.movie.tkts.dto;

import com.movie.tkts.entities.Movie;
import com.movie.tkts.entities.Theater;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private List<Integer> seats; //Todo
}

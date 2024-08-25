package com.movie.tkts.dto;

import com.movie.tkts.entities.Movie;
import com.movie.tkts.entities.Theater;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScreeningDto {

    private Long id;
    private LocalDate date;
    private LocalTime time;
    private Movie movie;
    private Theater theater;
}

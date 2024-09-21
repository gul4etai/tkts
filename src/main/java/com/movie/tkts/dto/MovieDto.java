package com.movie.tkts.dto;

import com.movie.tkts.entities.Screening;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {
    private Long movieId;
    private String title;
    private double price;
    private String genre;
    private int duration;
    private String imgURL;
    private String description;
    private List<ScreeningDto> screenings;
}



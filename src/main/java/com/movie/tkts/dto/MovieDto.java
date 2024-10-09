package com.movie.tkts.dto;

import com.movie.tkts.entities.Screening;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {
    private Long id;
    private String title;
    private double price;
    private String genre;
    private int duration;
    private String imgURL;
    private String description;
    private List<ScreeningDto> screenings;
}



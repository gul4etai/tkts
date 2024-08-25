package com.movie.tkts.dto;

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
    private String genre;
    private int ageGroup;
    private int duration;
    private String imgURL;
    private String description;
}

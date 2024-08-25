package com.movie.tkts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TheaterDto {
    private Long id;
    private String name;
    private int rows;
    private int seatsInRow;
    private int capacity;
}

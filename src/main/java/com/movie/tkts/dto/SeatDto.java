package com.movie.tkts.dto;

import com.movie.tkts.entities.Theater;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatDto {
    private Long id;
    int seatNum;
    private Theater theater;
}

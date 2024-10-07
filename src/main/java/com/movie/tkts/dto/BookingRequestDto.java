package com.movie.tkts.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class BookingRequestDto {

    private String userEmail;
    private Long movieId;
    private Long screeningId;
    private Long theaterId;
    private String date;
    private String time;
    private List<SeatDto> seats;
    }


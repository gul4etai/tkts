package com.movie.tkts.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
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


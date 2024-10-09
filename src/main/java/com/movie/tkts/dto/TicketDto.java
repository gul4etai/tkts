package com.movie.tkts.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto {
    private Long id;
    private Long bookingId;
    private Long seatId;
    //private int status;
    private Long screeningId;
}

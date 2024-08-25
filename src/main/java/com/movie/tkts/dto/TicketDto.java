package com.movie.tkts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto {
    private Long id;
    private Long bookingId;
    private Long seatId;
    private int status;
    private Long userId;
}

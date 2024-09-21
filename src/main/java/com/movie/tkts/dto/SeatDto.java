package com.movie.tkts.dto;

import com.movie.tkts.entities.Theater;
import com.movie.tkts.entities.Ticket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeatDto {
    private Long id;
    int seatNum;
    int rownNum;
    private Theater theater;
    private List<TicketDto> tickets;
}

package com.movie.tkts.dto;

import com.movie.tkts.entities.Screening;
import com.movie.tkts.entities.Ticket;
import com.movie.tkts.entities.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime bookingTime;
    private ScreeningDto screening; //returned
   // private Long screeningId;
    private String movieTitle;
    private Long userId;
    private List<TicketDto> tickets;
}

package com.movie.tkts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatDto {
    private Long id;
    @JsonProperty("seat")
    int seatNum;
    @JsonProperty("row")
    int rowNum;

//    private Theater theater;
//    private List<TicketDto> tickets;
}

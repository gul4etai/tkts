package com.movie.tkts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
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

package com.movie.tkts.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class BookingRequestDto {
        /*private Long screeningId;
        private Long userId;
        private List<Long> seatIds;*/
    private String userEmail;
    private Long movieId;
    private Long screeningId;
    private Long theaterId;
    private String date;  // Ensure this is properly formatted in the request
    private String time;  // Make sure time is properly formatted as well
    private List<SeatDto> seats;// Just the seat IDs, not the full Seat entity
    }


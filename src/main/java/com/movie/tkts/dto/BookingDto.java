package com.movie.tkts.dto;

import com.movie.tkts.entities.Screening;
import com.movie.tkts.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime bookingTime;
    private Screening screening;
    private User user;
}

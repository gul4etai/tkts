package com.movie.tkts.dto;


import com.movie.tkts.entities.Booking;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String password;
    private String email;
    private String username;
    private boolean isAdmin;
    private List<BookingDto> bookings;
}

package com.movie.tkts.dto;


import com.movie.tkts.entities.Booking;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    /**
     * {
     * "password": "1232",
     * "email": "user@mail.com",
     * "username": "nnne",
     * "admin":false
     * }
     **/
    private Long id;
    private String password;
    private String email;
    private String username;
    private boolean isAdmin;
    private List<BookingDto> bookings;
}

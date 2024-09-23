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

    /**
     {
     "password": "1232",
     "email": "user@mail.com",
     "username": "nnne",
     "isAdmin":false
     }
     **/
    private Long id;
    private String password;
    private String email;
    private String username;
    private boolean isAdmin;
    private List<BookingDto> bookings;
}

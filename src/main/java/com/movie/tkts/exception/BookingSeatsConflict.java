package com.movie.tkts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BookingSeatsConflict extends RuntimeException{
    public BookingSeatsConflict(String message) {
        super(message);
    }
}

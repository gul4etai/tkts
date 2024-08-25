package com.movie.tkts.controllers;

import com.movie.tkts.entities.Booking;
import com.movie.tkts.services.BookingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tkts/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public Booking createBooking(@RequestParam Long screeningId,
                                 @RequestParam Long userId,
                                 @RequestParam List<Long> seatIds) {
        return bookingService.createBooking(screeningId, userId, seatIds);
    }
}

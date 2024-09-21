package com.movie.tkts.controllers;

import com.movie.tkts.dto.BookingDto;
import com.movie.tkts.dto.SeatDto;
import com.movie.tkts.entities.Booking;
import com.movie.tkts.services.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tkts/bookings")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Endpoint to create a booking with multiple seats
    @PostMapping("/create")
    public ResponseEntity<BookingDto> createBooking(
            @RequestParam Long screeningId,
            @RequestParam Long userId,
            @RequestBody List<Long> seatIds) {
        BookingDto bookingDto = bookingService.createBooking(screeningId, userId, seatIds);
        return ResponseEntity.ok(bookingDto);
    }
/*    @PostMapping
    public Booking createBooking(@RequestParam Long screeningId,
                                 @RequestParam Long userId,
                                 @RequestParam List<Long> seatIds) {
        return bookingService.createBooking(screeningId, userId, seatIds);
    }*/
//method to gett a booking by id
    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    //method to get all bookings for a user
    @GetMapping("/user/{userId}")
    public List<BookingDto> getBookingsByUserId(@PathVariable Long userId) {
        return bookingService.getBookingsByUser(userId);
    }

/*    //method to get all bookings for a screening
    @GetMapping("/screening/{screeningId}")
    public List<BookingDto> getBookingsByScreeningId(@PathVariable Long screeningId) {
        return bookingService.getBookingsByScreeningId(screeningId);
    }*/
}


package com.movie.tkts.controllers;

import com.movie.tkts.dto.BookingDto;
import com.movie.tkts.dto.BookingRequestDto;
import com.movie.tkts.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/tkts/bookings")
@CrossOrigin(origins = "http://localhost:4200")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDto> createBooking(@RequestBody BookingRequestDto bookingRequestDto) {

        BookingDto createdBooking = bookingService.createBooking( bookingRequestDto);
        return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/user/{userId}")
    public List<BookingDto> getBookingsByUserId(@PathVariable Long userId) {
        return bookingService.getBookingsByUser(userId);
    }
}


package com.movie.tkts.services;

import com.movie.tkts.entities.*;
import com.movie.tkts.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private IBookingRepository bookingRepository;
    private ITicketRepository ticketRepository;
    private IScreeningRepository screeningRepository;
    private ISeatRepository seatRepository;
    private IUserRepository userRepository;

    public BookingService(IBookingRepository bookingRepository,
                          ITicketRepository ticketRepository,
                          IScreeningRepository screeningRepository,
                          ISeatRepository seatRepository,
                          IUserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Booking createBooking(Long screeningId, Long userId, List<Long> seatIds) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new RuntimeException("Screening not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setScreening(screening);
        booking.setBookingTime(LocalDateTime.now());
        booking = bookingRepository.save(booking);

        List<Seat> seats = seatRepository.findAllById(seatIds);
        for (Seat seat : seats) {
            Ticket bookedSeat = new Ticket();
            bookedSeat.setBooking(booking);
            bookedSeat.setSeat(seat);
            ticketRepository.save(bookedSeat);
        }

        return booking;
    }
}

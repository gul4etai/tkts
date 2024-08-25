package com.movie.tkts.services;

import com.movie.tkts.entities.Booking;
import com.movie.tkts.entities.Screening;
import com.movie.tkts.entities.Seat;
import com.movie.tkts.entities.Ticket;
import com.movie.tkts.repositories.IBookingRepository;
import com.movie.tkts.repositories.IScreeningRepository;
import com.movie.tkts.repositories.ISeatRepository;
import com.movie.tkts.repositories.ITicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private IBookingRepository bookingRepository;
    private ITicketRepository ticketRepository;
    private IScreeningRepository screeningRepository;
    private ISeatRepository seatRepository;

    public BookingService(IBookingRepository bookingRepository,
                          ITicketRepository ticketRepository,
                          IScreeningRepository screeningRepository,
                          ISeatRepository seatRepository) {
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
    }


    @Transactional
    public Booking createBooking(Long screeningId, Long userId, List<Long> seatIds) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new RuntimeException("Screening not found"));

        Booking booking = new Booking();
        booking.setId(userId); // Assuming a User entity is linked
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

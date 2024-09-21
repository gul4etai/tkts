package com.movie.tkts.services;

import com.movie.tkts.dto.BookingDto;
import com.movie.tkts.dto.SeatDto;
import com.movie.tkts.entities.*;
import com.movie.tkts.exception.ResourceNotFoundException;
import com.movie.tkts.mappers.impl.BookingMapperImpl;
import com.movie.tkts.mappers.impl.SeatMapperImpl;
import com.movie.tkts.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final IBookingRepository bookingRepository;
    private final ITicketRepository ticketRepository;
    private final IScreeningRepository screeningRepository;
    private final ISeatRepository seatRepository;
    private final IUserRepository userRepository;
    private final SeatMapperImpl seatMapper;
    private final BookingMapperImpl bookingMapper;

    public BookingService(IBookingRepository bookingRepository,
                          ITicketRepository ticketRepository,
                          IScreeningRepository screeningRepository,
                          ISeatRepository seatRepository,
                          IUserRepository userRepository,
                          SeatMapperImpl seatMapper, BookingMapperImpl bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
        this.seatMapper = seatMapper;
        this.bookingMapper = bookingMapper;
    }

    @Transactional
    public BookingDto createBooking(Long screeningId, Long userId, List<Long> seatIds) {
        // Find the screening
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new ResourceNotFoundException("Screening not found"));

        // Find the seats by their IDs
        List<Seat> seats = seatRepository.findAllById(seatIds);
        if (seats.size() != seatIds.size()) {
            throw new IllegalArgumentException("One or more seats not found.");
        }

        // Get the already booked seats for this screening via the Ticket entity
        List<Seat> alreadyBookedSeats = screening.getTickets().stream()
                .map(Ticket::getSeat)  // Extract seats from tickets
                .collect(Collectors.toList());

        // Check if any of the selected seats are already booked
        List<Seat> conflictingSeats = seats.stream()
                .filter(alreadyBookedSeats::contains)
                .collect(Collectors.toList());

        if (!conflictingSeats.isEmpty()) {
            throw new IllegalArgumentException("Some seats are already booked: " +
                    conflictingSeats.stream().map(Seat::getSeatNum).collect(Collectors.toList()));
        }

        // Create a new booking if no conflicts
        Booking booking = new Booking();
        booking.setScreening(screening);
        booking.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found")));
        booking.setBookingTime(LocalDateTime.now());

        // Create tickets for each booked seat
        List<Ticket> tickets = seats.stream().map(seat -> {
            Ticket ticket = new Ticket();
            ticket.setBooking(booking);
            ticket.setSeat(seat);
            ticket.setScreening(screening);
            ticket.setPrice(screening.getMovie().getPrice());  // Example price from movie
            ticket.setStatus(Ticket.TicketStatus.ACTIVE);  // Set initial status
            return ticket;
        }).collect(Collectors.toList());

        // Set tickets to the booking
        booking.setTickets(tickets);

        // Save the booking and tickets
        bookingRepository.save(booking);

        // Return the BookingDto
        return bookingMapper.toDto(booking);
    }

    // Method to fetch all bookings by user
    public List<BookingDto> getBookingsByUser(Long userId) {
        List<Booking> bookings = bookingRepository.findByUser_userId(userId);
        return bookings.stream()
                .map(bookingMapper::toDto)
                .collect(Collectors.toList());
    }

    // Method to fetch a booking by its ID
    public BookingDto getBookingById(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        return bookingMapper.toDto(booking);
    }

    @Transactional(readOnly = true)
    public List<SeatDto> getAvailableSeats(Long theaterId, Long screeningId) {
        try {
            if (screeningRepository.findById(screeningId).isEmpty()) {
                throw new IllegalStateException("Screening not found with id: " + screeningId);
            }
            if (seatRepository.findAllByTheater_TheaterId(theaterId).isEmpty()) {
                throw new IllegalStateException("No seats found for theater id: " + theaterId);
            }
            List<Seat> availableSeats = seatRepository.getAvailableSeats(theaterId);
            return availableSeats.stream()
                    .map(seatMapper::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println("Error fetching available seats: " + e.getMessage());
            throw e;  // Optionally rethrow or handle the exception as business logic demands
        }
    }

}

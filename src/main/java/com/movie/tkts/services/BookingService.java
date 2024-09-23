package com.movie.tkts.services;

import com.movie.tkts.dto.BookingDto;
import com.movie.tkts.dto.BookingRequestDto;
import com.movie.tkts.dto.SeatDto;
import com.movie.tkts.entities.*;
import com.movie.tkts.exception.ResourceNotFoundException;
import com.movie.tkts.mappers.impl.BookingMapperImpl;
import com.movie.tkts.mappers.impl.SeatMapperImpl;
import com.movie.tkts.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final IBookingRepository bookingRepository;
    private final ITicketRepository ticketRepository;
    private final ITheaterRepository theaterRepository;
    private final IScreeningRepository screeningRepository;
    private final ISeatRepository seatRepository;
    private final IUserRepository userRepository;
    private final SeatMapperImpl seatMapper;
    private final BookingMapperImpl bookingMapper;

    public BookingService(IBookingRepository bookingRepository,
                          ITicketRepository ticketRepository, ITheaterRepository theaterRepository,
                          IScreeningRepository screeningRepository,
                          ISeatRepository seatRepository,
                          IUserRepository userRepository,
                          SeatMapperImpl seatMapper, BookingMapperImpl bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.ticketRepository = ticketRepository;
        this.theaterRepository = theaterRepository;
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
        this.seatMapper = seatMapper;
        this.bookingMapper = bookingMapper;
    }


/**
 * {
 *   "userEmail": "user@mail.com",
 *   "movieId": 7,
 *   "screeningId": 3,
 *   "theaterId": 7,
 *   "date": "2023-09-21",
 *   "time": "12:00 PM",
 *   "seats": [
 *     { "row": 4, "seat": 3 },
 *     { "row": 2, "seat": 1 },
 *     { "row": 4, "seat": 1 },
 *     { "row": 6, "seat": 2 }
 *   ]
 * }**/

    @Transactional
    public BookingDto createBooking(BookingRequestDto bookingRequestDto) {
        // Find the user by email
        User user = userRepository.findByEmail(bookingRequestDto.getUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + bookingRequestDto.getUserEmail()));

        // Find the screening
        Screening screening = screeningRepository.findById(bookingRequestDto.getScreeningId())
                .orElseThrow(() -> new ResourceNotFoundException("Screening not found"));

        // Find the theater
        Theater theater = theaterRepository.findById(bookingRequestDto.getTheaterId())
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + bookingRequestDto.getTheaterId()));

        // Collect seats based on row and seat number from the request
        List<Seat> seatsToBook = new ArrayList<>();
        for (SeatDto seatDto : bookingRequestDto.getSeats()) {
            Seat seat = seatRepository.findByTheaterAndRowNumAndSeatNum(seatDto.getRowNum(), seatDto.getSeatNum(),theater.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Seat not found: row " + seatDto.getRowNum() + ", seat " + seatDto.getSeatNum()));
            seatsToBook.add(seat);
        }

        // Check for already booked seats in the screening
        List<Ticket> existingTickets = ticketRepository.findByScreeningId(screening.getId());
        List<Seat> alreadyBookedSeats = existingTickets.stream()
                .map(Ticket::getSeat)
                .collect(Collectors.toList());

        List<Seat> conflictingSeats = seatsToBook.stream()
                .filter(alreadyBookedSeats::contains)
                .collect(Collectors.toList());

        if (!conflictingSeats.isEmpty()) {
            throw new IllegalArgumentException("Some seats are already booked: " +
                    conflictingSeats.stream().map(seat -> "Row: " + seat.getRowNum() + ", Seat: " + seat.getSeatNum()).collect(Collectors.joining(", ")));
        }

        // Create a new booking
        Booking booking = new Booking();
        booking.setScreening(screening);
        booking.setUser(user);
        booking.setBookingTime(LocalDateTime.now());

        // Create tickets for each booked seat
        List<Ticket> tickets = seatsToBook.stream().map(seat -> {
            Ticket ticket = new Ticket();
            ticket.setBooking(booking);
            ticket.setSeat(seat);
            ticket.setScreening(screening);
            ticket.setPrice(screening.getMovie().getPrice());
            ticket.setStatus(Ticket.TicketStatus.ACTIVE);
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
        List<Booking> bookings = bookingRepository.findByUserId(userId);
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
            if (seatRepository.findAllByTheaterId(theaterId).isEmpty()) {
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

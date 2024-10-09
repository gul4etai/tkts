package com.movie.tkts.services;

import com.movie.tkts.dto.BookingDto;
import com.movie.tkts.dto.BookingRequestDto;
import com.movie.tkts.dto.SeatDto;
import com.movie.tkts.entities.*;
import com.movie.tkts.exception.BookingSeatsConflict;
import com.movie.tkts.exception.ResourceNotFoundException;
import com.movie.tkts.mappers.impl.BookingMapperImpl;
import com.movie.tkts.mappers.impl.SeatMapperImpl;
import com.movie.tkts.repositories.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + username));

        LocalDate date = LocalDate.parse(bookingRequestDto.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalTime time = LocalTime.parse(bookingRequestDto.getTime(), DateTimeFormatter.ofPattern("HH:mm:ss"));

        Screening screening = screeningRepository.findByMovieIdAndTheaterIdAndDateAndTime(
                        bookingRequestDto.getMovieId(),
                        bookingRequestDto.getTheaterId(),
                        date,
                        time)
                .orElseThrow(() -> new ResourceNotFoundException("Screening not found for the specified movie, theater, date, and time"));

        Theater theater = theaterRepository.findById(bookingRequestDto.getTheaterId())
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + bookingRequestDto.getTheaterId()));

        List<Seat> seatsToBook = new ArrayList<>();
        for (SeatDto seatDto : bookingRequestDto.getSeats()) {
            Seat seat = seatRepository.findByTheaterAndRowNumAndSeatNum(seatDto.getRowNum(), seatDto.getSeatNum(),theater.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Seat not found: row " + seatDto.getRowNum() + ", seat " + seatDto.getSeatNum()));
            seatsToBook.add(seat);
        }

        // check booked seets
        List<Ticket> existingTickets = ticketRepository.findByScreeningId(screening.getId());
        List<Seat> alreadyBookedSeats = existingTickets.stream()
                .map(Ticket::getSeat)
                .collect(Collectors.toList());

        List<Seat> conflictingSeats = seatsToBook.stream()
                .filter(alreadyBookedSeats::contains)
                .collect(Collectors.toList());

        if (!conflictingSeats.isEmpty()) {
            throw new BookingSeatsConflict("Some seats are already booked: " +
                    conflictingSeats.stream().map(seat -> "Row: " + seat.getRowNum() + ", Seat: " + seat.getSeatNum()).collect(Collectors.joining(", ")));
        }

        Booking booking = new Booking();
        booking.setScreening(screening);
        booking.setUser(user);
        booking.setBookingTime(LocalDateTime.now());
        //create tickets
        List<Ticket> tickets = seatsToBook.stream().map(seat -> {
            Ticket ticket = new Ticket();
            ticket.setBooking(booking);
            ticket.setSeat(seat);
            ticket.setScreening(screening);
            ticket.setPrice(screening.getMovie().getPrice());
            ticket.setStatus(Ticket.TicketStatus.ACTIVE);
            return ticket;
        }).collect(Collectors.toList());

        booking.setTickets(tickets);
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    public List<BookingDto> getBookingsByUser(String email) {
        List<Booking> bookings = bookingRepository.findByUserEmail(email);

        return bookings.stream()
                .map(booking -> {
                    BookingDto bookingDto = bookingMapper.toDto(booking);
                    String movieTitle = booking.getScreening().getMovie().getTitle();
                    bookingDto.setMovieTitle(movieTitle);

                    return bookingDto;
                })
                .collect(Collectors.toList());
    }

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
            throw e;
        }
    }

}

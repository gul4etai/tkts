package com.movie.tkts.services;

import com.movie.tkts.dto.ScreeningDto;
import com.movie.tkts.dto.SeatDto;
import com.movie.tkts.entities.Movie;
import com.movie.tkts.entities.Screening;
import com.movie.tkts.entities.Seat;
import com.movie.tkts.entities.Theater;
import com.movie.tkts.exception.ResourceNotFoundException;
import com.movie.tkts.mappers.impl.ScreeningMapperImpl;
import com.movie.tkts.mappers.impl.SeatMapperImpl;
import com.movie.tkts.repositories.IScreeningRepository;
import com.movie.tkts.repositories.ISeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.movie.tkts.entities.Ticket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScreeningService {

    private final IScreeningRepository screeningRepository;
    private final ISeatRepository seatRepository;
    private final ScreeningMapperImpl screeningMapper;
    private final SeatMapperImpl seatMapper;

    public ScreeningService(IScreeningRepository screeningRepository, ISeatRepository seatRepository, ScreeningMapperImpl screeningMapper, SeatMapperImpl seatMapper) {
        this.screeningRepository = screeningRepository;
        this.seatRepository = seatRepository;
        this.screeningMapper = screeningMapper;
        this.seatMapper = seatMapper;
    }

    @Transactional(readOnly = true)
    public List<ScreeningDto> getAllScreenings() {
        return (screeningRepository.findAll())
                .stream()
                .map(screeningMapper::toDto)
                .collect(Collectors.toList());
    }

    // Fetch booked seats for a screening
    public List<SeatDto> getBookedSeats(Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new ResourceNotFoundException("Screening not found"));

        // Get all booked seats for the screening
        List<Seat> bookedSeats = screening.getTickets().stream()
                .map(Ticket::getSeat)
                .collect(Collectors.toList());

        // Convert booked seats to DTO
        return bookedSeats.stream()
                .map(seatMapper::toDto)
                .collect(Collectors.toList());
    }

    // Fetch available seats for a screening
    public List<SeatDto> getAvailableSeats(Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new ResourceNotFoundException("Screening not found"));

        // Get all seats in the theater for this screening
        List<Seat> allSeats = seatRepository.findAllByTheaterId(screening.getTheater().getId());

        // Get all booked seats for the screening
        List<Seat> bookedSeats = screening.getTickets().stream()
                .map(Ticket::getSeat)
                .collect(Collectors.toList());

        // Filter out the booked seats from the total seats
        List<Seat> availableSeats = allSeats.stream()
                .filter(seat -> !bookedSeats.contains(seat))
                .collect(Collectors.toList());

        // Convert available seats to DTO
        return availableSeats.stream()
                .map(seatMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScreeningDto getScreeningById(Long id) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID " + id));
        return screeningMapper.toDto(screening);
    }

    @Transactional
    public ScreeningDto createScreening(ScreeningDto screening) {
        Screening savedScreening = screeningRepository.save(screeningMapper.toEntity(screening));
        return screeningMapper.toDto(savedScreening);
    }

 /*   @Transactional
    public Screening updateScreening(Long id, Screening screeningDetails) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Screening not found"));
        screening.setStartDate(screeningDetails.getStartDate());
        screening.setTime(screeningDetails.getTime());
        screening.setMovie(screeningDetails.getMovie());
        screening.setTheater(screeningDetails.getTheater());
        return screeningRepository.save(screening);
    }*/

    @Transactional
    public void deleteScreening(Long id) {
        screeningRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Screening> getScreeningsByDateAndTime(LocalDate date, LocalTime time) {
        return screeningRepository.findByDateAndTime(date, time);
    }

    @Transactional(readOnly = true)
    public List<Screening> getScreeningsByTheaterAndMovie(Theater theater, Movie movie) {
        return screeningRepository.findByTheaterAndMovie(theater, movie);
    }
}

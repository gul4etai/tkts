package com.movie.tkts.services;

import com.movie.tkts.dto.TheaterDto;
import com.movie.tkts.entities.Seat;
import com.movie.tkts.entities.Theater;
import com.movie.tkts.exception.ResourceNotFoundException;
import com.movie.tkts.mappers.impl.TheaterMapperImpl;
import com.movie.tkts.repositories.ITheaterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TheaterService {

    private final ITheaterRepository theaterRepository;
    private final TheaterMapperImpl theaterMapper;

    public TheaterService(ITheaterRepository theaterRepository, TheaterMapperImpl theaterMapper) {
        this.theaterRepository = theaterRepository;
        this.theaterMapper = theaterMapper;
    }

    @Transactional(readOnly = true)
    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Theater> getTheaterById(Long id) {
        return theaterRepository.findById(id);
    }

    @Transactional
    public TheaterDto createTheater(TheaterDto theaterDto) {
        // Convert DTO to entity
        Theater theater = theaterMapper.toEntity(theaterDto);

        // Auto-generate seats
        List<Seat> seats = new ArrayList<>();
        for (int rowNum = 1; rowNum <= theater.getRows(); rowNum++) {
            for (int seatNum = 1; seatNum <= theater.getSeatsInRow(); seatNum++) {
                Seat seat = new Seat();
                seat.setRowNum(rowNum);
                seat.setSeatNum(seatNum);
                seat.setTheater(theater);
                seats.add(seat);
            }
        }
        theater.setSeats(seats);

        // Save the theater with the auto-generated seats
        Theater savedTheater = theaterRepository.save(theater);

        // Return the DTO
        return theaterMapper.toDto(savedTheater);
    }

    @Transactional
    public TheaterDto updateTheater(Long theaterId, TheaterDto theaterDto) {
        // Fetch the existing theater
        Theater existingTheater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + theaterId));

        // Update basic fields
        existingTheater.setName(theaterDto.getName());

        // Check if the rows or seats in row changed
        if (theaterDto.getRows() != existingTheater.getRows() || theaterDto.getSeatsInRow() != existingTheater.getSeatsInRow()) {
            // If rows or seats in row changed, regenerate the seats
            List<Seat> newSeats = new ArrayList<>();
            for (int rowNum = 1; rowNum <= theaterDto.getRows(); rowNum++) {
                for (int seatNum = 1; seatNum <= theaterDto.getSeatsInRow(); seatNum++) {
                    Seat seat = new Seat();
                    seat.setRowNum(rowNum);
                    seat.setSeatNum(seatNum);
                    seat.setTheater(existingTheater);
                    newSeats.add(seat);
                }
            }
            existingTheater.setSeats(newSeats);  // Replace old seats with new ones
        }

        // Update the theater entity in the database
        Theater updatedTheater = theaterRepository.save(existingTheater);
        return theaterMapper.toDto(updatedTheater);
    }

    @Transactional
    public void deleteTheater(Long theaterId) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + theaterId));

        // Delete the theater (this will cascade to delete seats and screenings)
        theaterRepository.delete(theater);
    }
}

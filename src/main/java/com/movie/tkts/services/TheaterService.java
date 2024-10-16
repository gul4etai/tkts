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
import java.util.stream.Collectors;

@Service
public class TheaterService {

    private final ITheaterRepository theaterRepository;
    private final TheaterMapperImpl theaterMapper;

    public TheaterService(ITheaterRepository theaterRepository, TheaterMapperImpl theaterMapper) {
        this.theaterRepository = theaterRepository;
        this.theaterMapper = theaterMapper;
    }

    @Transactional(readOnly = true)
    public List<TheaterDto> getAllTheaters() {
        List<Theater> theaters = theaterRepository.findAll();
        return theaters.stream()
                .map(theaterMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<Theater> getTheaterById(Long id) {
        return theaterRepository.findById(id);
    }

    @Transactional
    public TheaterDto createTheater(TheaterDto theaterDto) {
        Theater theater = theaterMapper.toEntity(theaterDto);

        // generate seats
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
        Theater savedTheater = theaterRepository.save(theater);
        return theaterMapper.toDto(savedTheater);
    }

    @Transactional
    public TheaterDto updateTheater(Long theaterId, TheaterDto theaterDto) {
        Theater existingTheater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + theaterId));

        existingTheater.setName(theaterDto.getName());

        if (theaterDto.getRows() != existingTheater.getRows() || theaterDto.getSeatsInRow() != existingTheater.getSeatsInRow()) {
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
            existingTheater.setSeats(newSeats);
        }

        Theater updatedTheater = theaterRepository.save(existingTheater);
        return theaterMapper.toDto(updatedTheater);
    }

    @Transactional
    public void deleteTheater(Long theaterId) {
        Theater theater = theaterRepository.findById(theaterId)
                .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + theaterId));

        theaterRepository.delete(theater);
    }
}

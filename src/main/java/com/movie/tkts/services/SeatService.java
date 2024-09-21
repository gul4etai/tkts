package com.movie.tkts.services;

import com.movie.tkts.dto.SeatDto;
import com.movie.tkts.entities.Seat;
import com.movie.tkts.mappers.impl.SeatMapperImpl;
import com.movie.tkts.repositories.ISeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {
    private final ISeatRepository seatRepository;
    private final SeatMapperImpl seatMapper;

    public SeatService(ISeatRepository seatRepository, SeatMapperImpl seatMapper) {
        this.seatRepository = seatRepository;
        this.seatMapper = seatMapper;
    }

    @Transactional(readOnly = true)
    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Seat> getSeatById(Long id) {
        return seatRepository.findById(id);
    }

    @Transactional
    public Seat createSeat(Seat seat) {
        return seatRepository.save(seat);
    }

    @Transactional
    public Seat updateSeat(Long id, Seat seatDetails) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
        seat.setSeatNum(seatDetails.getSeatNum());
        seat.setTheater(seatDetails.getTheater());
        return seatRepository.save(seat);
    }

    @Transactional
    public void deleteSeat(Long id) {
        seatRepository.deleteById(id);
    }

    public List<SeatDto> getAvailableSeats(Long screeningId) {
        List<Seat> availableSeats = seatRepository.getAvailableSeats(screeningId);
        return availableSeats.stream().map(seatMapper::toDto).toList();
    }
}

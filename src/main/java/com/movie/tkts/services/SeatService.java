package com.movie.tkts.services;

import com.movie.tkts.entities.Seat;
import com.movie.tkts.repositories.ISeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {
    private final ISeatRepository seatRepository;

    public SeatService(ISeatRepository seatRepository) {
        this.seatRepository = seatRepository;
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
}

package com.movie.tkts.controllers;

import com.movie.tkts.dto.SeatDto;
import com.movie.tkts.entities.Seat;
import com.movie.tkts.services.SeatService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tkts/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping
    public List<Seat> getAllSeats() {
        return seatService.getAllSeats();
    }

    @GetMapping("/{id}")
    public Optional<Seat> getSeatById(@PathVariable Long id) {
        return seatService.getSeatById(id);
    }

    @PostMapping
    public Seat createSeat(@RequestBody Seat seat) {
        return seatService.createSeat(seat);
    }

    @PutMapping("/{id}")
    public Seat updateSeat(@PathVariable Long id, @RequestBody Seat seatDetails) {
        return seatService.updateSeat(id, seatDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
    }

    //getmapping to get available seats for a screening in a spesific theater
    @GetMapping("/screening/{screeningId}/available-seats")
    public List<SeatDto> getAvailableSeats(@PathVariable Long screeningId) {
        return seatService.getAvailableSeats(screeningId);
    }
}

package com.movie.tkts.controllers;

import com.movie.tkts.dto.TheaterDto;
import com.movie.tkts.dto.ScreeningDto;
import com.movie.tkts.entities.Theater;
import com.movie.tkts.services.ScreeningService;
import com.movie.tkts.services.TheaterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tkts/theaters")
public class TheaterController {

    private final TheaterService theaterService;
    private final ScreeningService screeningService;

    public TheaterController(TheaterService theaterService, ScreeningService screeningService) {
        this.theaterService = theaterService;
        this.screeningService = screeningService;
    }

    @GetMapping
    public ResponseEntity<List<Theater>> getAllTheaters() {
        List<Theater> theaters = theaterService.getAllTheaters();
        return ResponseEntity.ok(theaters);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theater> getTheaterById(@PathVariable Long id) {
        Optional<Theater> theater = theaterService.getTheaterById(id);
        return theater.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get screenings by theater ID
    @GetMapping("/{id}/screenings")
    public ResponseEntity<List<ScreeningDto>> getScreeningsByTheaterId(@PathVariable("id") Long id) {
        List<ScreeningDto> screenings = screeningService.getScreeningsByTheaterId(id);
        if (screenings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(screenings);
    }

    @PostMapping
    public ResponseEntity<TheaterDto> createTheater(@RequestBody TheaterDto theaterDto) {
        TheaterDto savedTheater = theaterService.createTheater(theaterDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTheater);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TheaterDto> updateTheater(
            @PathVariable Long id,
            @RequestBody TheaterDto theaterDto) {
        TheaterDto updatedTheater = theaterService.updateTheater(id, theaterDto);
        return ResponseEntity.ok(updatedTheater);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id) {
        theaterService.deleteTheater(id);
        return ResponseEntity.noContent().build();
    }
}

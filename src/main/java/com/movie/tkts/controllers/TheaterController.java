package com.movie.tkts.controllers;

import com.movie.tkts.entities.Theater;
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

    public TheaterController(TheaterService theaterService) {
        this.theaterService = theaterService;
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

    @PostMapping
    public ResponseEntity<Theater> createTheater(@RequestBody Theater theater) {
        Theater createdTheater = theaterService.createTheater(theater);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTheater);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Theater> updateTheater(@PathVariable Long id, @RequestBody Theater updatedTheater) {
        try {
            Theater theater = theaterService.updateTheater(id, updatedTheater);
            return ResponseEntity.ok(theater);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheater(@PathVariable Long id) {
        try {
            theaterService.deleteTheater(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

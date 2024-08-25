package com.movie.tkts.controllers;

import com.movie.tkts.entities.Screening;
import com.movie.tkts.services.ScreeningService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/screenings")
public class ScreeningController {

    private final ScreeningService screeningService;

    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @GetMapping
    public List<Screening> getAllScreenings() {
        return screeningService.getAllScreenings();
    }

    @GetMapping("/{id}")
    public Optional<Screening> getScreeningById(@PathVariable Long id) {
        return screeningService.getScreeningById(id);
    }

    @PostMapping
    public Screening createScreening(@RequestBody Screening screening) {
        return screeningService.createScreening(screening);
    }

    @PutMapping("/{id}")
    public Screening updateScreening(@PathVariable Long id, @RequestBody Screening screeningDetails) {
        return screeningService.updateScreening(id, screeningDetails);
    }

    @DeleteMapping("/{id}")
    public void deleteScreening(@PathVariable Long id) {
        screeningService.deleteScreening(id);
    }

    @GetMapping("/search")
    public List<Screening> getScreeningsByDateAndTime(
            @RequestParam LocalDate date,
            @RequestParam LocalTime time) {
        return screeningService.getScreeningsByDateAndTime(date, time);
    }
}


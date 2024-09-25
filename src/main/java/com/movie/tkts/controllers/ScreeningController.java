package com.movie.tkts.controllers;

import com.movie.tkts.dto.MovieDto;
import com.movie.tkts.dto.ScreeningDto;
import com.movie.tkts.dto.SeatDto;
import com.movie.tkts.dto.TheaterDto;
import com.movie.tkts.entities.Movie;
import com.movie.tkts.entities.Screening;
import com.movie.tkts.exception.ResourceNotFoundException;
import com.movie.tkts.mappers.impl.MovieMapperImpl;
import com.movie.tkts.mappers.impl.ScreeningMapperImpl;
import com.movie.tkts.mappers.impl.TheaterMapperImpl;
import com.movie.tkts.services.ScreeningService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tkts/screenings")
@CrossOrigin(origins = "http://localhost:4200")
public class ScreeningController {

    private final ScreeningService screeningService;
    private final TheaterMapperImpl theaterMapper;
    private final MovieMapperImpl movieMapper;


    public ScreeningController(ScreeningService screeningService,
                               TheaterMapperImpl theaterMapper,
                               MovieMapperImpl movieMapper, ScreeningMapperImpl screeningMapper) {
        this.screeningService = screeningService;
        this.theaterMapper = theaterMapper;
        this.movieMapper = movieMapper;
    }

    @GetMapping
    public List<ScreeningDto> getAllScreenings() {
        return screeningService.getAllScreenings();
    }

    // Endpoint to get booked seats for a screening
    @GetMapping("/{screeningId}/booked-seats")
    public ResponseEntity<List<SeatDto>> getBookedSeats(@PathVariable Long screeningId) {
        List<SeatDto> bookedSeats = screeningService.getBookedSeats(screeningId);
        return ResponseEntity.ok(bookedSeats);
    }

    // Endpoint to get available seats for a screening
    @GetMapping("/{screeningId}/available-seats")
    public ResponseEntity<List<SeatDto>> getAvailableSeats(@PathVariable Long screeningId) {
        List<SeatDto> availableSeats = screeningService.getAvailableSeats(screeningId);
        return ResponseEntity.ok(availableSeats);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ScreeningDto> getScreeningById(@PathVariable Long id) {
        ScreeningDto screeningDto = screeningService.getScreeningById(id);
        return new ResponseEntity<>(screeningDto, HttpStatus.OK);

        /*Optional<Screening> returnedScreening = screeningService.getScreeningById(id);
        if (returnedScreening.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Screening screening = returnedScreening.get();
        ScreeningDto screeningDto = screeningMapper.toDto(screening);
        return new ResponseEntity<>(screeningDto, HttpStatus.OK);*/

        /*Optional<Screening> returnedScreening = screeningService.getScreeningById(id);
        return returnedScreening.map(screening -> {
            ScreeningDto screeningDto = screeningMapper.toDto(screening);
            return new ResponseEntity<>(screeningDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));*/
    }

    @PostMapping
    public ResponseEntity<ScreeningDto> createScreening(@RequestBody ScreeningDto screening) {
        ScreeningDto newScreening = screeningService.createScreening(screening);
        return new ResponseEntity<>(newScreening, HttpStatus.CREATED);
    }

   /* @PutMapping("/{id}")
    public ResponseEntity<ScreeningDto> updateScreening(@PathVariable Long id, @RequestBody Screening screeningDetails) {
        return screeningService.updateScreening(id, screeningDetails);
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity deleteScreening(@PathVariable Long id) {
        screeningService.deleteScreening(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search/date-time")
    public List<Screening> getScreeningsByDateAndTime(
            @RequestParam LocalDate date,
            @RequestParam LocalTime time) {
        return screeningService.getScreeningsByDateAndTime(date, time);
    }

   /* @GetMapping("/search/theater-movie")
    public List<Screening> getScreeningsByTheaterAndMovie(
            @RequestParam TheaterDto theaterDto,
            @RequestParam MovieDto movieDto) {
        return screeningService.getScreeningsByTheaterAndMovie(theaterMapper.toEntity(theaterDto), movieMapper.toEntity(movieDto));
    }*/
//get method to get all screenings for a specific movie
  /*  @GetMapping("/movie/{movieId}")
    public List<ScreeningDto> getScreeningsByMovie(@PathVariable Long movieId) {
        return screeningService.getScreeningsByMovie(movieId);
    }*/
}


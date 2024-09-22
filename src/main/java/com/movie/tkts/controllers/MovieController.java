package com.movie.tkts.controllers;

import com.movie.tkts.dto.MovieDto;
import com.movie.tkts.services.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tkts/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }
    // Create a new movie with or without screenings
    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto movieDto) {
        MovieDto createdMovie = movieService.createMovie(movieDto);
        return ResponseEntity.ok(createdMovie);
    }

    // Update an existing movie with any combination of fields
    @PutMapping("/{movieId}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long movieId, @RequestBody MovieDto movieDto) {
        MovieDto updatedMovie = movieService.updateMovie(movieId, movieDto);
        return ResponseEntity.ok(updatedMovie);
    }

    // Delete a movie by ID
    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.noContent().build();
    }

    // Get a list of all movies
    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        List<MovieDto> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    // Get a list of all movies by a specific theater
    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<MovieDto>> getMoviesByTheater(@PathVariable Long theaterId) {
        List<MovieDto> movies = movieService.getMoviesByTheater(theaterId);
        return ResponseEntity.ok(movies);
    }

    // Get a movie by ID
    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long movieId) {
        MovieDto movie = movieService.getMovieById(movieId);
        return ResponseEntity.ok(movie);
    }

    // Get the most booked movies within a given date range
    @GetMapping("/most-booked")
    public ResponseEntity<List<MovieDto>> getMostBookedMovies(@RequestParam String startDate, @RequestParam String endDate) {
        List<MovieDto> movies = movieService.getMostBookedMovies(startDate, endDate);
        return ResponseEntity.ok(movies);
    }

    // Get the least booked movies within a given date range
    @GetMapping("/least-booked")
    public ResponseEntity<List<MovieDto>> getLeastBookedMovies(@RequestParam String startDate, @RequestParam String endDate) {
        List<MovieDto> movies = movieService.getLeastBookedMovies(startDate, endDate);
        return ResponseEntity.ok(movies);
    }

}

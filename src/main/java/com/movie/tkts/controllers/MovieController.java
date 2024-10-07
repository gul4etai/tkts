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
@CrossOrigin(origins = "http://localhost:4200")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto movieDto) {
        MovieDto createdMovie = movieService.createMovie(movieDto);
        return ResponseEntity.ok(createdMovie);
    }

    @PutMapping("/{movieId}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long movieId, @RequestBody MovieDto movieDto) {
        MovieDto updatedMovie = movieService.updateMovie(movieId, movieDto);
        return ResponseEntity.ok(updatedMovie);
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long movieId) {
        movieService.deleteMovie(movieId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        List<MovieDto> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/theater/{theaterId}")
    public ResponseEntity<List<MovieDto>> getMoviesByTheater(@PathVariable Long theaterId) {
        List<MovieDto> movies = movieService.getMoviesByTheater(theaterId);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{movieId}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long movieId) {
        MovieDto movie = movieService.getMovieById(movieId);
        return ResponseEntity.ok(movie);
    }

    @GetMapping("/most-booked/{startDate}/{endDate}")
    public ResponseEntity<List<MovieDto>> getMostBookedMovies(@PathVariable String startDate, @PathVariable String endDate) {
        List<MovieDto> movies = movieService.getMostBookedMovies(startDate, endDate);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/least-booked/{startDate}/{endDate}")
    public ResponseEntity<List<MovieDto>> getLeastBookedMovies(@PathVariable String startDate, @PathVariable String endDate) {
        List<MovieDto> movies = movieService.getLeastBookedMovies(startDate, endDate);
        return ResponseEntity.ok(movies);
    }

}

package com.movie.tkts.controllers;

import com.movie.tkts.dto.MovieDto;
import com.movie.tkts.entities.Movie;
import com.movie.tkts.mappers.IMapper;
import com.movie.tkts.services.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tkts/movies")
public class MovieController {

    private MovieService movieService;
    private IMapper<Movie, MovieDto> movieMapper;

    public MovieController (IMapper<Movie, MovieDto> movieMapper, MovieService movieService ){
        this.movieMapper = movieMapper;
        this.movieService = movieService;
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        Optional<Movie> foundMovie = movieService.getMovieById(id);
        return foundMovie.map(Movie -> {
            MovieDto movieDto = movieMapper.toDto(Movie);
            return new ResponseEntity<>(movieDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

//    @PostMapping
//    public Movie createMovie(@RequestBody Movie movie) {
//        return movieService.saveMovie(movie);
//    }

    @PostMapping
    public ResponseEntity<MovieDto> createMovie(@RequestBody MovieDto movieDto) {
        Movie movie = movieMapper.toEntity(movieDto);
        Movie savedMovie = movieService.saveMovie(movie);
        return new ResponseEntity<>(movieMapper.toDto(savedMovie), HttpStatus.CREATED);
    }

//    @PutMapping("/{id}")
//    public Movie updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
//        movie.setId(id);
//        return movieService.saveMovie(movie);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteMovie(@PathVariable Long id) {
//        movieService.deleteMovie(id);
//    }
}

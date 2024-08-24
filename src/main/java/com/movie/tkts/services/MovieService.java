package com.movie.tkts.services;

import com.movie.tkts.entities.Movie;
import com.movie.tkts.repositories.IMovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MovieService {

    private IMovieRepository movieRepository;

    public MovieService(IMovieRepository movieRepository ){
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll().stream().collect(Collectors.toList());
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public Movie saveMovie (Movie movie) {
        return movieRepository.save(movie);
    }
}

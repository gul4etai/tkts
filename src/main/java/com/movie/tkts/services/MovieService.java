package com.movie.tkts.services;

import com.movie.tkts.dto.ScreeningDto;
import com.movie.tkts.entities.Movie;
import com.movie.tkts.entities.Screening;
import com.movie.tkts.exception.ResourceNotFoundException;
import com.movie.tkts.mappers.impl.MovieMapperImpl;
import com.movie.tkts.mappers.impl.ScreeningMapperImpl;
import com.movie.tkts.repositories.IMovieRepository;
import com.movie.tkts.dto.MovieDto;
import com.movie.tkts.repositories.IScreeningRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final IMovieRepository movieRepository;
    private final IScreeningRepository screeningRepository;
    private final MovieMapperImpl movieMapper;
    private final ScreeningMapperImpl screeningMapper;
    @Value("${file.upload-dir}")
    private String uploadDir;

    public MovieService(IMovieRepository movieRepository, IScreeningRepository screeningRepository, MovieMapperImpl movieMapper, ScreeningMapperImpl screeningMapper) {
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
        this.movieMapper = movieMapper;
        this.screeningMapper = screeningMapper;
    }

    @Transactional
    public MovieDto createMovie(MovieDto movieDto) {
        // Convert DTO to entity
        Movie movie = movieMapper.toEntity(movieDto);

        // Save the movie first (without screenings)
        Movie savedMovie = movieRepository.save(movie);

        // Handle screenings separately
        if (movieDto.getScreenings() != null) {
            for (ScreeningDto screeningDto : movieDto.getScreenings()) {
                Screening screening = screeningMapper.toEntity(screeningDto);
                screening.setMovie(savedMovie);  // Link screening to the saved movie

                // Save each screening
                screeningRepository.save(screening);
            }
        }

        // Return the saved movie as a DTO
        return movieMapper.toDto(savedMovie);
    }


    @Transactional
    public MovieDto updateMovie(Long movieId, MovieDto movieDto) {
        // Fetch the movie
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        // Update movie fields
        if (movieDto.getTitle() != null) {
            movie.setTitle(movieDto.getTitle());
        }

        if (movieDto.getGenre() != null) {
            movie.setGenre(movieDto.getGenre());
        }

        if (movieDto.getDuration() != 0) {
            movie.setDuration(movieDto.getDuration());
        }

        if (movieDto.getPrice() != 0) {
            movie.setPrice(movieDto.getPrice());
        }

        if (movieDto.getImgURL() != null) {
            movie.setImgURL(movieDto.getImgURL());
        }

        if (movieDto.getDescription() != null) {
            movie.setDescription(movieDto.getDescription());
        }

        // Save the movie first (without screenings)
        Movie updatedMovie = movieRepository.save(movie);

        // Handle screenings update or addition separately
        if (movieDto.getScreenings() != null) {
            for (ScreeningDto screeningDto : movieDto.getScreenings()) {
                Screening screening;

                if (screeningDto.getId() != null) {
                    // Update existing screening
                    screening = screeningRepository.findById(screeningDto.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Screening not found"));
                    screening.setDate(screeningDto.getDate());
                    screening.setTime(screeningDto.getTime());
                    screening.setTheater(screeningMapper.toEntity(screeningDto).getTheater());
                } else {
                    // Create new screening
                    screening = screeningMapper.toEntity(screeningDto);
                    screening.setMovie(movie); // Link screening to the movie
                }

                // Save or update the screening
                screeningRepository.save(screening);
            }
        }

        return movieMapper.toDto(updatedMovie);
    }


    @Transactional
    public void deleteMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        movieRepository.delete(movie);
    }

    @Transactional(readOnly = true)
    public List<MovieDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream().map(movieMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovieDto> getMoviesByTheater(Long theaterId) {
        List<Movie> movies = movieRepository.findByTheaterId(theaterId);
        return movies.stream().map(movieMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MovieDto getMovieById(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        return movieMapper.toDto(movie);
    }

    @Transactional(readOnly = true)
    public List<MovieDto> getMostBookedMovies(String startDate, String endDate) {
        List<Movie> movies = movieRepository.findMostBookedMovies(startDate, endDate);
        return movies.stream().map(movieMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovieDto> getLeastBookedMovies(String startDate, String endDate) {
        List<Movie> movies = movieRepository.findLeastBookedMovies(startDate, endDate);
        return movies.stream().map(movieMapper::toDto).collect(Collectors.toList());
    }

}


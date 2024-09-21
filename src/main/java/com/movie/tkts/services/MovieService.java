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
        Movie movie = movieMapper.toEntity(movieDto);

        // If screenings are provided, set the relationship to the movie
        if (movie.getScreenings() != null) {
            for (Screening screening : movie.getScreenings()) {
                screening.setMovie(movie);
            }
        }

        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toDto(savedMovie);
    }

    @Transactional
    public MovieDto updateMovie(Long movieId, MovieDto movieDto) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

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

        // Handle screenings update or addition
        if (movieDto.getScreenings() != null) {
            for (ScreeningDto screeningDto : movieDto.getScreenings()) {
                Screening screening;
                if (screeningDto.getId() != null) {
                    screening = screeningRepository.findById(screeningDto.getId())
                            .orElseThrow(() -> new ResourceNotFoundException("Screening not found"));
                    screening.setDate(screeningDto.getDate());
                    screening.setTime(screeningDto.getTime());
                    screening.setTheater(screeningMapper.toEntity(screeningDto).getTheater());
                } else {
                    screening = screeningMapper.toEntity(screeningDto);
                    screening.setMovie(movie);
                    movie.getScreenings().add(screening);
                }
            }
        }

        Movie updatedMovie = movieRepository.save(movie);
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
  /*   @Transactional
    public MovieDto createMovie(MovieDto movieDto, MultipartFile image) throws IOException {
        Movie movie = movieMapper.toEntity(movieDto);

        if (image != null && !image.isEmpty()) {
            String fileName = saveImage(image);
            movie.setImgURL("/movieImgs/" + fileName); // Assuming a relative path
        }
        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toDto(savedMovie);
    }

   @Transactional
    public MovieDto updateMovie(Long movieId, MovieDto movieDto, MultipartFile image) throws IOException {
        if (!movieRepository.existsById(movieId)) {
            throw new RuntimeException("Movie not found");
        }

        Movie movie = movieMapper.toEntity(movieDto);
        movie.setMovieId(movieId);

        if (image != null && !image.isEmpty()) {
            String fileName = saveImage(image);
            movie.setImgURL("/Images/" + fileName); // Assuming a relative path
        }

        Movie updatedMovie = movieRepository.save(movie);
        return movieMapper.toDto(updatedMovie);
    }

    private String saveImage(MultipartFile image) throws IOException {
        String fileName = image.getOriginalFilename();
        File fileSaveDir = new File(uploadDir);

        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdirs();
        }

        File imageFile = new File(fileSaveDir, fileName);
        image.transferTo(imageFile);
        return fileName;
    }*/


}


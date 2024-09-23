package com.movie.tkts.services;

import com.movie.tkts.dto.ScreeningDto;
import com.movie.tkts.entities.*;
import com.movie.tkts.exception.ResourceNotFoundException;
import com.movie.tkts.mappers.impl.MovieMapperImpl;
import com.movie.tkts.mappers.impl.ScreeningMapperImpl;
import com.movie.tkts.mappers.impl.SeatMapperImpl;
import com.movie.tkts.mappers.impl.TheaterMapperImpl;
import com.movie.tkts.repositories.IMovieRepository;
import com.movie.tkts.dto.MovieDto;
import com.movie.tkts.repositories.IScreeningRepository;
import com.movie.tkts.repositories.ITheaterRepository;
import com.movie.tkts.repositories.ITicketRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final IMovieRepository movieRepository;
    private final IScreeningRepository screeningRepository;
    private final ITicketRepository ticketRepository;
    private final MovieMapperImpl movieMapper;
    private final ScreeningMapperImpl screeningMapper;
    private final TheaterMapperImpl  theaterMapper;

    private final ITheaterRepository theaterRepository;
    private final SeatMapperImpl seatMapper;
    private final TheaterService theaterService;
    @Value("${file.upload-dir}")
    private String uploadDir;

    public MovieService(IMovieRepository movieRepository, IScreeningRepository screeningRepository, ITicketRepository ticketRepository, MovieMapperImpl movieMapper, ScreeningMapperImpl screeningMapper, TheaterMapperImpl theaterMapper, ITheaterRepository theaterRepository, SeatMapperImpl seetMapper, TheaterService theaterService) {
        this.movieRepository = movieRepository;
        this.screeningRepository = screeningRepository;
        this.ticketRepository = ticketRepository;
        this.movieMapper = movieMapper;
        this.screeningMapper = screeningMapper;
        this.theaterMapper = theaterMapper;
        this.theaterRepository = theaterRepository;
        this.seatMapper = seetMapper;
        this.theaterService = theaterService;
    }

    @Transactional
   /* public MovieDto createMovie(MovieDto movieDto) {
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
    }*/

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

                // Fetch the Theater entity using the theaterId in screeningDto
                Theater theater = theaterRepository.findById(screeningDto.getTheaterId())
                        .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + screeningDto.getTheaterId()));
                screening.setTheater(theater);  // Set the theater for the screening

                // Save each screening
                screeningRepository.save(screening);
            }
        }

        // Return the saved movie as a DTO
        return movieMapper.toDto(savedMovie);
    }

//    @Transactional
//    public MovieDto updateMovie(Long movieId, MovieDto movieDto) {
//        // Fetch the movie
//        Movie movie = movieRepository.findById(movieId)
//                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
//
//        // Update movie fields
//        if (movieDto.getTitle() != null) {
//            movie.setTitle(movieDto.getTitle());
//        }
//
//        if (movieDto.getGenre() != null) {
//            movie.setGenre(movieDto.getGenre());
//        }
//
//        if (movieDto.getDuration() != 0) {
//            movie.setDuration(movieDto.getDuration());
//        }
//
//        if (movieDto.getPrice() != 0) {
//            movie.setPrice(movieDto.getPrice());
//        }
//
//        if (movieDto.getImgURL() != null) {
//            movie.setImgURL(movieDto.getImgURL());
//        }
//
//        if (movieDto.getDescription() != null) {
//            movie.setDescription(movieDto.getDescription());
//        }
//
//        // Save the movie first (without screenings)
//        Movie updatedMovie = movieRepository.save(movie);
//
//        // Existing screenings in the database
//        List<Screening> existingScreenings = screeningRepository.findByMovieId(movieId);
//
//        // Handle screenings update or addition separately
//        if (movieDto.getScreenings() != null) {
//            List<Long> updatedScreeningIds = new ArrayList<>();
//
//            for (ScreeningDto screeningDto : movieDto.getScreenings()) {
//                Screening screening;
//
//                if (screeningDto.getId() != null) {
//                    // Update existing screening
//                    screening = screeningRepository.findById(screeningDto.getId())
//                            .orElseThrow(() -> new ResourceNotFoundException("Screening not found"));
//                    screening.setDate(screeningDto.getDate());
//                    screening.setTime(screeningDto.getTime());
//
//                    // Set the existing theater by its ID (without recreating it)
//                    Theater theater = theaterRepository.findById(screeningDto.getTheaterId())
//                            .orElseThrow(() -> new ResourceNotFoundException("Theater not found"));
//                    screening.setTheater(theater);
//
//                } else {
//                    // Create new screening
//                    screening = screeningMapper.toEntity(screeningDto);
//                    screening.setMovie(movie); // Link screening to the movie
//
//                    // Set the theater by its ID
//                    Theater theater = theaterRepository.findById(screeningDto.getTheaterId())
//                            .orElseThrow(() -> new ResourceNotFoundException("Theater not found"));
//                    screening.setTheater(theater);
//                }
//
//                // Save or update the screening
//                screeningRepository.save(screening);
//
//                // Track the screening ID for later removal check
//                updatedScreeningIds.add(screening.getId());
//            }
//
//            // Remove screenings that are no longer present in the updated list (optional)
//            existingScreenings.stream()
//                    .filter(screening -> !updatedScreeningIds.contains(screening.getId()))
//                    .forEach(screening -> screeningRepository.delete(screening));
//        }
//
//        return movieMapper.toDto(updatedMovie);
//    }


    @Transactional
    public MovieDto updateMovie(Long movieId, MovieDto updatedMovieDto) {
        // Find the existing movie
        Movie existingMovie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        // Update movie details (if provided)
        existingMovie.setTitle(updatedMovieDto.getTitle());
        existingMovie.setPrice(updatedMovieDto.getPrice());
        existingMovie.setGenre(updatedMovieDto.getGenre());
        existingMovie.setDuration(updatedMovieDto.getDuration());
        existingMovie.setImgURL(updatedMovieDto.getImgURL());
        existingMovie.setDescription(updatedMovieDto.getDescription());

        // Save the updated movie details first (without updating screenings yet)
        movieRepository.save(existingMovie);

        // Update screenings (if provided)
        if (updatedMovieDto.getScreenings() != null) {
            // Find existing screenings for the movie
            List<Screening> existingScreenings = screeningRepository.findByMovieId(movieId);

            // Convert DTOs to Screening entities
            List<Screening> updatedScreenings = updatedMovieDto.getScreenings().stream().map(screeningDto -> {
                Screening screening = screeningMapper.toEntity(screeningDto);
                screening.setMovie(existingMovie); // Link screening to the updated movie

                // Fetch the Theater entity using the theaterId in ScreeningDto
                Theater theater = theaterRepository.findById(screeningDto.getTheaterId())
                        .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + screeningDto.getTheaterId()));
                screening.setTheater(theater); // Set the theater for the screening

                return screening;
            }).collect(Collectors.toList());

            // Remove screenings that are no longer present in the updated list
            List<Long> updatedScreeningIds = updatedScreenings.stream().map(Screening::getId).collect(Collectors.toList());
            existingScreenings.stream()
                    .filter(existingScreening -> !updatedScreeningIds.contains(existingScreening.getId()))
                    .forEach(screeningToRemove -> {
                        screeningRepository.delete(screeningToRemove);
                    });

            // Save the new and updated screenings
            screeningRepository.saveAll(updatedScreenings);
        }

        // Fetch the updated screenings manually
        List<Screening> updatedScreenings = screeningRepository.findByMovieId(movieId);

        // Map the updated screenings to ScreeningDto
        List<ScreeningDto> screeningDtos = updatedScreenings.stream()
                .map(screeningMapper::toDto)
                .collect(Collectors.toList());

        // Map the movie to MovieDto (with screenings)
        MovieDto movieDto = movieMapper.toDto(existingMovie);
        movieDto.setScreenings(screeningDtos);

        // Return the updated movie as a DTO with screenings
        return movieDto;
    }




/*
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
*/


    @Transactional
    public void deleteMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        movieRepository.delete(movie);
    }



    @Transactional(readOnly = true)
    public List<MovieDto> getMoviesByTheater(Long theaterId) {
        List<Movie> movies = movieRepository.findByTheaterId(theaterId);
        return movies.stream().map(movieMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public List<MovieDto> getAllMovies() {
        // Fetch all movies from the repository
        List<Movie> movies = movieRepository.findAll();

        // Map movies to MovieDto and handle screenings manually
        List<MovieDto> movieDtos = movies.stream().map(movie -> {
            // Map the movie to MovieDto (without screenings)
            MovieDto movieDto = movieMapper.toDto(movie);

            // Fetch the screenings manually for each movie
            List<Screening> screenings = screeningRepository.findByMovieId(movie.getId());

            // Map screenings to ScreeningDto and fetch occupied seats
            List<ScreeningDto> screeningDtos = screenings.stream().map(screening -> {
                ScreeningDto screeningDto = screeningMapper.toDto(screening);

                // Fetch tickets for this screening to get the occupied seats
                List<Ticket> tickets = ticketRepository.findByScreeningId(screening.getId());

                // Map occupied seats in the format [[rowNum, seatNum]]
                List<int[]> occupiedSeats = tickets.stream()
                        .map(ticket -> new int[]{ticket.getSeat().getRowNum(), ticket.getSeat().getSeatNum()})
                        .collect(Collectors.toList());

                // Set occupied seats in ScreeningDto
                screeningDto.setOccupiedSeats(occupiedSeats);

                return screeningDto;
            }).collect(Collectors.toList());

            // Set the screenings list in the MovieDto
            movieDto.setScreenings(screeningDtos);

            return movieDto;
        }).collect(Collectors.toList());

        return movieDtos;
    }



    /**{
     *
     * getmoviebyid
     *
        "movieId": 1,
                "title": "Avatars lfe",
                "price": 15.99,
                "genre": "Sci-Fi",
                "duration": 162,
                "imgURL": "avatar.jpg",
                "description": "A visually stunning world of Pandora.",
                "screenings": [
        {
            "id": 2,
                "date": "2024-10-01",
                "time": "18:00:00",
                "movieId": 1,
                "theaterId": 6,
                "occupiedSeats": [
        [1, 2],
        [4, 0]
      ]
        },
        {
            "id": 3,
                "date": "2024-10-02",
                "time": "20:00:00",
                "movieId": 1,
                "theaterId": 7,
                "occupiedSeats": []
        }
  ]
    }
**/


    @Transactional
    public MovieDto getMovieById(Long movieId) {
        // Fetch the movie by its ID
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        // Map the movie to MovieDto (without screenings)
        MovieDto movieDto = movieMapper.toDto(movie);

        // Fetch the screenings manually for this movie
        List<Screening> screenings = screeningRepository.findByMovieId(movieId);

        // Map screenings to ScreeningDto and fetch occupied seats
        List<ScreeningDto> screeningDtos = screenings.stream().map(screening -> {
            ScreeningDto screeningDto = screeningMapper.toDto(screening);

            // Fetch tickets for this screening to get the occupied seats
            List<Ticket> tickets = ticketRepository.findByScreeningId(screening.getId());

            // Map occupied seats in the format [[rowNum, seatNum]]
            List<int[]> occupiedSeats = tickets.stream()
                    .map(ticket -> new int[]{ticket.getSeat().getRowNum(), ticket.getSeat().getSeatNum()})
                    .collect(Collectors.toList());

            // Set occupied seats in ScreeningDto
            screeningDto.setOccupiedSeats(occupiedSeats);

            return screeningDto;
        }).collect(Collectors.toList());

        // Set the screenings list in the MovieDto
        movieDto.setScreenings(screeningDtos);

        return movieDto;
    }





 /*   //all seats in theter
 @Transactional(readOnly = true)
    public MovieDto getMovieById(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

        // Convert movie to DTO
        MovieDto movieDto = movieMapper.toDto(movie);

        // Fetch screenings for the movie
        List<Screening> screenings = screeningRepository.findByMovieId(movieId);

        // Convert screenings to DTOs and set them in the MovieDto
        List<ScreeningDto> screeningDtos = screenings.stream()
                .map(screeningMapper::toDto)
                .collect(Collectors.toList());

        movieDto.setScreenings(screeningDtos);  // Set screenings in the MovieDto

        return movieDto;
    }*/

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


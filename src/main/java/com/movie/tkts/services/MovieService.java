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

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
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
    public MovieDto createMovie(MovieDto movieDto) {
        // Set the movie's ID to null to ensure it's treated as a new entity
        movieDto.setId(null);

        // Convert DTO to entity and save the movie first (without screenings)
        Movie movie = movieMapper.toEntity(movieDto);

        // Handle screenings separately and link them to the movie before saving the movie
        if (movieDto.getScreenings() != null) {
            List<Screening> screenings = new ArrayList<>();
            for (ScreeningDto screeningDto : movieDto.getScreenings()) {
                screeningDto.setId(null);  // Set the screening ID to null to ensure it's treated as a new entity
                Screening screening = screeningMapper.toEntity(screeningDto);
                screening.setMovie(movie);  // Link screening to the unsaved movie

                // Fetch the Theater entity using the theaterId in screeningDto
                Theater theater = theaterRepository.findById(screeningDto.getTheaterId())
                        .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + screeningDto.getTheaterId()));
                screening.setTheater(theater);  // Set the theater for the screening

                screenings.add(screening);
            }
            // Link the screenings to the movie before persisting the movie
            movie.setScreenings(screenings);
        }

        // Save the movie with screenings at once
        Movie savedMovie = movieRepository.save(movie);

        // Return the saved movie as a DTO
        return movieMapper.toDto(savedMovie);
    }

  /*  @Transactional
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


        // Update screenings (if provided)
        if (updatedMovieDto.getScreenings() != null) {
            // Find existing screenings for the movie
            List<Screening> existingScreenings = screeningRepository.findByMovieId(movieId); //overflow

            // Convert DTOs to Screening entities
            List<Screening> updatedScreenings = new ArrayList<>();
            for (ScreeningDto screeningDto : updatedMovieDto.getScreenings()) {
                // Map ScreeningDto to Screening entity
                Screening screening = screeningMapper.toEntity(screeningDto);
                screening.setMovie(existingMovie); // Link screening to the updated movie

                // Fetch the Theater entity using the theaterId in ScreeningDto
                Theater theater = theaterRepository.findById(screeningDto.getTheaterId())
                        .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + screeningDto.getTheaterId()));
                screening.setTheater(theater); // Set the theater for the screening

                // Add the screening to the list
                updatedScreenings.add(screening);
            }


            // Remove screenings that are no longer present in the updated list
            // Create a list to store the IDs of updated screenings
            List<Long> updatedScreeningIds = new ArrayList<>();
            for (Screening updatedScreening : updatedScreenings) {
                updatedScreeningIds.add(updatedScreening.getId());
            }

// Iterate over existing screenings and remove any that are not in the updated screenings list
            for (Screening existingScreening : existingScreenings) {
                if (!updatedScreeningIds.contains(existingScreening.getId())) {
                    screeningRepository.delete(existingScreening);
                }
            }

            // Save the new and updated screenings
            screeningRepository.saveAll(updatedScreenings);
        }

        // Fetch the updated screenings manually
        List<Screening> updatedScreenings = screeningRepository.findByMovie_Id(movieId);


        List<ScreeningDto> screeningDtos = new ArrayList<>();
        for (Screening screening : updatedScreenings) {
            screeningDtos.add(screeningMapper.toDto(screening));
        }

        movieRepository.save(existingMovie);
        // Map the movie to MovieDto (with screenings)
        MovieDto movieDto = movieMapper.toDto(existingMovie);
        movieDto.setScreenings(screeningDtos);

        // Return the updated movie as a DTO with screenings
        return movieDto;
    } latest 7.10
*/
  @Transactional
  public MovieDto updateMovie(Long movieId, MovieDto updatedMovieDto) {
      // Find the existing movie
      Movie existingMovie = movieRepository.findById(movieId)
              .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

          System.out.println("Number of screenings for movie {}: {}" + existingMovie.getTitle() + existingMovie.getScreenings().size());
      for (Screening screening : existingMovie.getScreenings()) {
          System.out.println("Screening date: {}, time: {}" + screening.getDate() + screening.getTime());
      }

      // Update movie details (if provided)
      existingMovie.setTitle(updatedMovieDto.getTitle());
      existingMovie.setPrice(updatedMovieDto.getPrice());
      existingMovie.setGenre(updatedMovieDto.getGenre());
      existingMovie.setDuration(updatedMovieDto.getDuration());
      existingMovie.setImgURL(updatedMovieDto.getImgURL());
      existingMovie.setDescription(updatedMovieDto.getDescription());

      // Save the updated movie details first (without updating screenings yet)
      movieRepository.save(existingMovie);

      // Handle screenings separately
      List<Screening> existingScreenings = screeningRepository.findByMovie_Id(movieId);
      List<Screening> updatedScreenings = new ArrayList<>();

      // Convert DTOs to Screening entities
      for (ScreeningDto screeningDto : updatedMovieDto.getScreenings()) {
          Screening screening = screeningMapper.toEntity(screeningDto);
          screening.setMovie(existingMovie); // Link screening to the updated movie

          // Fetch the Theater entity using the theaterId in ScreeningDto
          Theater theater = theaterRepository.findById(screeningDto.getTheaterId())
                  .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + screeningDto.getTheaterId()));
          screening.setTheater(theater); // Set the theater for the screening

          updatedScreenings.add(screening);
      }

      // Update existing screenings and identify which screenings to delete
      updateScreenings(existingScreenings, updatedScreenings, existingMovie);

      // Fetch the updated screenings manually after saving
      List<Screening> finalScreenings = screeningRepository.findByMovie_Id(movieId);
      List<ScreeningDto> screeningDtos = finalScreenings.stream()
              .map(screeningMapper::toDto)
              .collect(Collectors.toList());

      // Set the updated screenings in the MovieDto
      MovieDto movieDto = movieMapper.toDto(existingMovie);
      movieDto.setScreenings(screeningDtos);

      System.out.println("Number of screenings for movie {}: {}" + movieDto.getTitle() + movieDto.getScreenings().size());
      for (ScreeningDto screening : movieDto.getScreenings()) {
          System.out.println("Screening date: {}, time: {}" + screening.getDate() + screening.getTime());
      }


      //??
     // movieRepository.save(existingMovie);

      // Return the updated movie as a DTO with screenings
      return movieDto;
  }

    private void updateScreenings(List<Screening> existingScreenings, List<Screening> updatedScreenings, Movie movie) {
        // Create a map of existing screenings by ID for easy lookup
        Map<Long, Screening> existingScreeningMap = existingScreenings.stream()
                .collect(Collectors.toMap(Screening::getId, Function.identity()));

        Map<Long, Screening> updatedScreeningMap = updatedScreenings.stream()
                .collect(Collectors.toMap(Screening::getId, Function.identity()));

        // Remove screenings that are not in the updated list
        Iterator<Screening> iterator = movie.getScreenings().iterator();
        while (iterator.hasNext()) {
            Screening existingScreening = iterator.next();
            if (!updatedScreeningMap.containsKey(existingScreening.getId())) {
                iterator.remove();  // Remove the screening from the movie's list
                screeningRepository.deleteById(existingScreening.getId());  // Delete the screening from the repository
            }
        }

        // if not in existing screenings, add
        for (Screening updatedScreening : updatedScreenings) {
            if (!existingScreeningMap.containsKey(updatedScreening.getId())) {
                screeningRepository.save(updatedScreening);
            }
        }
        //if not in updated screenings, remove
      /*  for (Screening exsistingScreening : existingScreenings){
            if (!updatedScreeningMap.containsKey(exsistingScreening.getId())){
                screeningRepository.deleteById(exsistingScreening.getId());
            }
        }*/
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
        List<MovieDto> movieDtos = new ArrayList<>();
        for (Movie movie : movies) {
           // movie.getScreenings().clear(); //to map screenings manually
            // Map the movie to MovieDto (without screenings)
            MovieDto movieDto = movieMapper.toDto(movie);

            // Fetch the screenings manually for each movie
            List<Screening> screenings = movie.getScreenings();//screeningRepository.findByMovieId(movie.getId());

            // Map screenings to ScreeningDto and fetch occupied seats
            List<ScreeningDto> screeningDtos = new ArrayList<>();
            for (Screening screening : screenings) {
                ScreeningDto screeningDto = screeningMapper.toDto(screening);

                // Fetch tickets for this screening to get the occupied seats
                List<Ticket> tickets = ticketRepository.findByScreeningId(screening.getId());

                // Map occupied seats in the format [[rowNum, seatNum]]
                List<int[]> occupiedSeats = new ArrayList<>();
                for (Ticket ticket : tickets) {
                    int[] seatCoordinates = new int[]{ticket.getSeat().getRowNum(), ticket.getSeat().getSeatNum()};
                    occupiedSeats.add(seatCoordinates);
                }

                // Set occupied seats in ScreeningDto
                screeningDto.setOccupiedSeats(occupiedSeats);

                screeningDtos.add(screeningDto);
            }

            // Set the screenings list in the MovieDto
            movieDto.setScreenings(screeningDtos);

            movieDtos.add(movieDto);
        }

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

    @Transactional(readOnly = true)
    public List<MovieDto> getMostBookedMovies(String startDate, String endDate) {
        // Parse the string parameters into LocalDate
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // Call the repository method with LocalDate parameters
        List<Movie> movies = movieRepository.findMostBookedMovies(start, end);
        return movies.stream().map(movieMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovieDto> getLeastBookedMovies(String startDate, String endDate) {
        // Parse the string parameters into LocalDate
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        // Call the repository method with LocalDate parameters
        List<Movie> movies = movieRepository.findLeastBookedMovies(start, end);
        return movies.stream().map(movieMapper::toDto).collect(Collectors.toList());
    }
}


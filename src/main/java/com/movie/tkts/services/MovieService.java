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
        movieDto.setId(null);
        Movie movie = movieMapper.toEntity(movieDto);

        if (movieDto.getScreenings() != null) {
            List<Screening> screenings = new ArrayList<>();
            for (ScreeningDto screeningDto : movieDto.getScreenings()) {
                screeningDto.setId(null);
                Screening screening = screeningMapper.toEntity(screeningDto);
                screening.setMovie(movie);

                Theater theater = theaterRepository.findById(screeningDto.getTheaterId())
                        .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + screeningDto.getTheaterId()));
                screening.setTheater(theater);

                screenings.add(screening);
            }
            // Link the screenings to the movie before save
            movie.setScreenings(screenings);
        }

        Movie savedMovie = movieRepository.save(movie);
        return movieMapper.toDto(savedMovie);
    }


  @Transactional
  public MovieDto updateMovie(Long movieId, MovieDto updatedMovieDto) {

      Movie existingMovie = movieRepository.findById(movieId)
              .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));

      existingMovie.setTitle(updatedMovieDto.getTitle());
      existingMovie.setPrice(updatedMovieDto.getPrice());
      existingMovie.setGenre(updatedMovieDto.getGenre());
      existingMovie.setDuration(updatedMovieDto.getDuration());
      existingMovie.setImgURL(updatedMovieDto.getImgURL());
      existingMovie.setDescription(updatedMovieDto.getDescription());

      movieRepository.save(existingMovie);

      // Handle screenings separately
      List<Screening> existingScreenings = screeningRepository.findByMovie_Id(movieId);
      List<Screening> updatedScreenings = new ArrayList<>();

      for (ScreeningDto screeningDto : updatedMovieDto.getScreenings()) {
          Screening screening = screeningMapper.toEntity(screeningDto);
          screening.setMovie(existingMovie); // link screening to the updated movie

          Theater theater = theaterRepository.findById(screeningDto.getTheaterId())
                  .orElseThrow(() -> new ResourceNotFoundException("Theater not found with id: " + screeningDto.getTheaterId()));
          screening.setTheater(theater); // Set the theater for the screening
          updatedScreenings.add(screening);
      }

      updateScreenings(existingScreenings, updatedScreenings, existingMovie);

      // get updated screenings from repository
      List<Screening> finalScreenings = screeningRepository.findByMovie_Id(movieId);
      List<ScreeningDto> screeningDtos = finalScreenings.stream()
              .map(screeningMapper::toDto)
              .collect(Collectors.toList());

      // Set the updated screenings in the MovieDto
      MovieDto movieDto = movieMapper.toDto(existingMovie);
      movieDto.setScreenings(screeningDtos);
      return movieDto;
  }

    private void updateScreenings(List<Screening> existingScreenings, List<Screening> updatedScreenings, Movie movie) {

        Map<Long, Screening> existingScreeningMap = existingScreenings.stream()
                .collect(Collectors.toMap(Screening::getId, Function.identity()));

        Map<Long, Screening> updatedScreeningMap = updatedScreenings.stream()
                .collect(Collectors.toMap(Screening::getId, Function.identity()));

        // Remove screenings that are not in the updated list
        Iterator<Screening> iterator = movie.getScreenings().iterator();
        while (iterator.hasNext()) {
            Screening existingScreening = iterator.next();
            if (!updatedScreeningMap.containsKey(existingScreening.getId())) {
                iterator.remove();  // remove the screening from list
                screeningRepository.deleteById(existingScreening.getId());
            }
        }

        // if not in existing screenings, add
        for (Screening updatedScreening : updatedScreenings) {
            if (!existingScreeningMap.containsKey(updatedScreening.getId())) {
                screeningRepository.save(updatedScreening);
            }
        }
    }


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
        List<Movie> movies = movieRepository.findAll();
        List<MovieDto> movieDtos = new ArrayList<>();
        for (Movie movie : movies) {
            MovieDto movieDto = movieMapper.toDto(movie);

            List<Screening> screenings = movie.getScreenings();//screeningRepository.findByMovieId(movie.getId());
            // get occupied seats for screenings
            List<ScreeningDto> screeningDtos = new ArrayList<>();
            for (Screening screening : screenings) {
                ScreeningDto screeningDto = screeningMapper.toDto(screening);

                List<Ticket> tickets = ticketRepository.findByScreeningId(screening.getId());
                // map occupied seats
                List<int[]> occupiedSeats = new ArrayList<>();
                for (Ticket ticket : tickets) {
                    int[] seatCoordinates = new int[]{ticket.getSeat().getRowNum(), ticket.getSeat().getSeatNum()};
                    occupiedSeats.add(seatCoordinates);
                }

                screeningDto.setOccupiedSeats(occupiedSeats);
                screeningDtos.add(screeningDto);
            }
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
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found"));
        MovieDto movieDto = movieMapper.toDto(movie);

        // get the screenings
        List<Screening> screenings = screeningRepository.findByMovieId(movieId);
        List<ScreeningDto> screeningDtos = screenings.stream().map(screening -> {
            ScreeningDto screeningDto = screeningMapper.toDto(screening);
            List<Ticket> tickets = ticketRepository.findByScreeningId(screening.getId());

            // map occupied seats [[rowNum, seatNum]]
            List<int[]> occupiedSeats = tickets.stream()
                    .map(ticket -> new int[]{ticket.getSeat().getRowNum(), ticket.getSeat().getSeatNum()})
                    .collect(Collectors.toList());
            screeningDto.setOccupiedSeats(occupiedSeats);

            return screeningDto;
        }).collect(Collectors.toList());

        movieDto.setScreenings(screeningDtos);

        return movieDto;
    }

    @Transactional(readOnly = true)
    public List<MovieDto> getMostBookedMovies(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<Movie> movies = movieRepository.findMostBookedMovies(start, end);
        return movies.stream().map(movieMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MovieDto> getLeastBookedMovies(String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<Movie> movies = movieRepository.findLeastBookedMovies(start, end);
        return movies.stream().map(movieMapper::toDto).collect(Collectors.toList());
    }
}


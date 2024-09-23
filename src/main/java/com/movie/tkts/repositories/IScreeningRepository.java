package com.movie.tkts.repositories;

import com.movie.tkts.entities.Movie;
import com.movie.tkts.entities.Screening;
import com.movie.tkts.entities.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface IScreeningRepository extends JpaRepository <Screening, Long> {

   // Optional<Screening> findById(Long id);

    List<Screening> findByDateAndTime(LocalDate date, LocalTime time);

    List<Screening> findByTheaterAndMovie(Theater theater, Movie movie);

    List<Screening> findByTheaterId(Long theaterId);

    @Query("SELECT s FROM Screening s WHERE s.movie.id = :movieId")
    List<Screening> findByMovieId(@Param("movieId") Long movieId);

    @Query("SELECT s FROM Screening s JOIN s.tickets t WHERE s.movie.id = :movieId")
    List<Screening> findScreeningsWithBookedSeatsByMovieId(Long movieId);
}

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


    List<Screening> findByDateAndTime(LocalDate date, LocalTime time);

    List<Screening> findByTheaterAndMovie(Theater theater, Movie movie);

    List<Screening> findByTheaterId(Long theaterId);

    //@Query("SELECT s FROM Screening s WHERE s.movie.id = :movieId")
    @Query("SELECT s.id, s.date, s.time, s.theater.id FROM Screening s WHERE s.movie.id = :movieId")
    List<Screening> findByMovieId(@Param("movieId") Long movieId);

   /* @Query("SELECT s FROM Screening s JOIN s.tickets t WHERE s.movie.id = :movieId")
    List<Screening> findScreeningsWithBookedSeatsByMovieId(Long movieId);*/

    @Query("SELECT s FROM Screening s WHERE s.movie.id = :movieId AND s.theater.id = :theaterId AND s.date = :date AND s.time = :time")
    Optional<Screening> findScreening(Long movieId, Long theaterId, String date, String time);

    Optional<Screening> findByMovieIdAndTheaterIdAndDateAndTime(Long movieId, Long theaterId, LocalDate parse, LocalTime parse1);

    List<Screening> findByMovie_Id(Long movieId);
}

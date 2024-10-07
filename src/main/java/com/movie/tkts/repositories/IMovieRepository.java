package com.movie.tkts.repositories;

import com.movie.tkts.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IMovieRepository extends JpaRepository<Movie, Long> {

    @Query("SELECT DISTINCT m FROM Movie m JOIN Screening s ON m.id = s.movie.id WHERE s.theater.id = :theaterId")
    List<Movie> findByTheaterId(@Param("theaterId") Long theaterId);

    @Query("SELECT m FROM Movie m JOIN Screening s ON m.id = s.movie.id JOIN s.tickets t " +
            "WHERE s.date BETWEEN :startDate AND :endDate " +
            "GROUP BY m ORDER BY COUNT(t) DESC")
    List<Movie> findMostBookedMovies(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT m FROM Movie m LEFT JOIN Screening s ON m.id = s.movie.id LEFT JOIN s.tickets t " +
            "WHERE s.date BETWEEN :startDate AND :endDate OR s.date IS NULL " +
            "GROUP BY m ORDER BY COUNT(t) ASC")
    List<Movie> findLeastBookedMovies(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

}





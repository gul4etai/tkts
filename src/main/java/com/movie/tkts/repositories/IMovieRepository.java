package com.movie.tkts.repositories;

import com.movie.tkts.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMovieRepository extends JpaRepository<Movie, Long> {

    // Get movies by theater ID
    @Query("SELECT DISTINCT m FROM Movie m JOIN m.screenings s WHERE s.theater.id = :theaterId")
    List<Movie> findByTheaterId(@Param("theaterId") Long theaterId);

    // Most booked movies in a given date range
    @Query("SELECT m FROM Movie m JOIN m.screenings s JOIN s.tickets t " +
            "WHERE s.date BETWEEN :startDate AND :endDate " +
            "GROUP BY m ORDER BY COUNT(t) DESC")
    List<Movie> findMostBookedMovies(@Param("startDate") String startDate, @Param("endDate") String endDate);

    // Least booked movies in a given date range
    @Query("SELECT m FROM Movie m JOIN m.screenings s JOIN s.tickets t " +
            "WHERE s.date BETWEEN :startDate AND :endDate " +
            "GROUP BY m ORDER BY COUNT(t) ASC")
    List<Movie> findLeastBookedMovies(@Param("startDate") String startDate, @Param("endDate") String endDate);
}





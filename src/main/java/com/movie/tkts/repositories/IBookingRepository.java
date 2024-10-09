package com.movie.tkts.repositories;

import com.movie.tkts.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IBookingRepository extends JpaRepository<Booking, Long> {

  /*  @Query("SELECT b.screening.movie, COUNT(b) AS booking_count " +
            "FROM Booking b " +
            "WHERE b.bookingTime BETWEEN :startDate AND :endDate " +
            "GROUP BY b.screening.movie " +
            "ORDER BY booking_count DESC")
    List<Object[]> findMostWatchedMovies(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);*/

    List<Booking> findByUserId(Long userId);

    List<Booking> findByUserEmail(String email);
}

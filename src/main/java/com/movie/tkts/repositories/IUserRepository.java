package com.movie.tkts.repositories;

import com.movie.tkts.dto.UserDto;
import com.movie.tkts.entities.Movie;
import com.movie.tkts.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.bookings b " +
            "WHERE b.bookingTime BETWEEN :startDate AND :endDate " +
            "GROUP BY u ORDER BY COUNT(b) DESC")
    List<User> findMostActiveUsers(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT u FROM User u LEFT JOIN u.bookings b ON b.bookingTime BETWEEN :startDate AND :endDate " +
            "GROUP BY u ORDER BY COUNT(b) ASC")
    List<User> findLeastActiveUsers(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}

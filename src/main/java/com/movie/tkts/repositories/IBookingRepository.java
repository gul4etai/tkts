package com.movie.tkts.repositories;

import com.movie.tkts.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface IBookingRepository extends JpaRepository<Booking, Long> {


    List<Booking> findByUserId(Long userId);

    List<Booking> findByUserEmail(String email);
}

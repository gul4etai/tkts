package com.movie.tkts.repositories;

import com.movie.tkts.entities.Booking;
import com.movie.tkts.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IBookingRepository extends JpaRepository<Booking, Long> {
}

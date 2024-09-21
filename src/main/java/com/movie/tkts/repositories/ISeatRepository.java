package com.movie.tkts.repositories;

import com.movie.tkts.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ISeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findAllByTheater_TheaterId(Long theaterId);


    @Query("SELECT s FROM Seat s WHERE s.theater.theaterId = :theaterId AND s NOT IN (SELECT t.seat FROM Ticket t WHERE t.screening.screeningId = :screeningId)")
    List<Seat> getAvailableSeats(Long screeningId);
}

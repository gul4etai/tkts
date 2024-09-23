package com.movie.tkts.repositories;

import com.movie.tkts.entities.Seat;
import com.movie.tkts.entities.Theater;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ISeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findAllByTheaterId(Long theaterId);


    @Query("SELECT s FROM Seat s WHERE s.theater.id = :theaterId AND s NOT IN (SELECT t.seat FROM Ticket t WHERE t.screening.id = :screeningId)")
    List<Seat> getAvailableSeats(Long screeningId);

    @Query("SELECT s FROM Seat s WHERE s.theater.id = :theaterId AND s.id NOT IN " +
            "(SELECT t.seat.id FROM Ticket t WHERE t.screening.date = :date AND t.screening.time = :time AND t.status = 'ACTIVE')")
    List<Seat> findSeatsByTheaterDateTime(Long theaterId, String date, String time);

    @Query("SELECT s FROM Seat s WHERE s.rowNum = :row AND s.seatNum = :seatNum AND s.theater.id = :theaterId")
    Optional<Seat> findByTheaterAndRowNumAndSeatNum(@Param("row") int row, @Param("seatNum") int seatNum, @Param("theaterId") Long theaterId);

}

package com.movie.tkts.repositories;

import com.movie.tkts.entities.Seat;
import com.movie.tkts.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Collection;
import java.util.List;

public interface ITicketRepository extends JpaRepository<Ticket, Long> {

    boolean existsBySeatIdAndScreeningId(Long seatId, Long screeningId);

    List<Ticket> findByScreeningId(Long screeningId);

    @Query("SELECT t.seat FROM Ticket t WHERE t.screening.id = :screeningId")
    List<Seat> findBookedSeatsByScreening(@Param("screeningId") Long screeningId);



}

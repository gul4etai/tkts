package com.movie.tkts.repositories;

import com.movie.tkts.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ITicketRepository extends JpaRepository<Ticket, Long> {

    boolean existsBySeatIdAndScreeningId(Long seatId, Long screeningId);

    Collection<Ticket> findByScreeningId(Long screeningId);

}

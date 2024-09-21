package com.movie.tkts.repositories;

import com.movie.tkts.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface ITicketRepository extends JpaRepository<Ticket, Long> {

    boolean existsBySeat_seatIdAndScreening_screeningId(Long seatId, Long screeningId);

    Collection<Ticket> findByScreening_screeningId(Long screeningId);

}

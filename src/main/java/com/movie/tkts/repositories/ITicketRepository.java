package com.movie.tkts.repositories;

import com.movie.tkts.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITicketRepository extends JpaRepository<Ticket, Long> {
}

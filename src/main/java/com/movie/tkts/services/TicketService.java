package com.movie.tkts.services;

import com.movie.tkts.entities.Ticket;
import com.movie.tkts.repositories.ITicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    private final ITicketRepository ticketRepository;

    public TicketService(ITicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional(readOnly = true)
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Ticket> getTicketById(Long id) {
        return ticketRepository.findById(id);
    }

    @Transactional
    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Transactional
    public Ticket updateTicket(Long id, Ticket ticketDetails) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));
        ticket.setBooking(ticketDetails.getBooking());
        ticket.setSeat(ticketDetails.getSeat());
        ticket.setStatus(ticketDetails.getStatus());
        return ticketRepository.save(ticket);
    }

    @Transactional
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}

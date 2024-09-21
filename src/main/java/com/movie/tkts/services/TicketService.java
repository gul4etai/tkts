package com.movie.tkts.services;

import com.movie.tkts.dto.TicketDto;
import com.movie.tkts.entities.Ticket;
import com.movie.tkts.mappers.impl.TicketMapperImpl;
import com.movie.tkts.repositories.IBookingRepository;
import com.movie.tkts.repositories.IScreeningRepository;
import com.movie.tkts.repositories.ISeatRepository;
import com.movie.tkts.repositories.ITicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TicketService {
    private final ITicketRepository ticketRepository;
    private final ISeatRepository seatRepository;
    private final IScreeningRepository screeningRepository;
    private final IBookingRepository orderRepository;
    private final TicketMapperImpl ticketMapper;
    private final TicketMapperImpl ticketMapperImpl;

    public TicketService(ITicketRepository ticketRepository, ISeatRepository seatRepository,
                         IScreeningRepository screeningRepository, IBookingRepository orderRepository,
                         TicketMapperImpl ticketMapper, TicketMapperImpl ticketMapperImpl) {
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
        this.screeningRepository = screeningRepository;
        this.orderRepository = orderRepository;
        this.ticketMapper = ticketMapper;
        this.ticketMapperImpl = ticketMapperImpl;
    }

    @Transactional
    public TicketDto orderTicket(TicketDto ticketDTO) {
        // Check if the seat is already taken for the screening
        boolean seatTaken = ticketRepository.existsBySeat_seatIdAndScreening_screeningId(
                ticketDTO.getSeatId(), ticketDTO.getScreeningId()
        );
        if (seatTaken) {
            throw new RuntimeException("The seat is already taken for this screening.");
        }
        // Convert DTO to entity
        Ticket ticket = ticketMapperImpl.toEntity(ticketDTO);

        // Save the ticket entity to the database
        ticket = ticketRepository.save(ticket);

        // Convert the saved entity back to DTO and return it
        return ticketMapper.toDto(ticket);
    }



//    private final ITicketRepository ticketRepository;
//
//    public TicketService(ITicketRepository ticketRepository) {
//        this.ticketRepository = ticketRepository;
//    }
//
//    @Transactional(readOnly = true)
//    public List<Ticket> getAllTickets() {
//        return ticketRepository.findAll();
//    }
//
//    @Transactional(readOnly = true)
//    public Optional<Ticket> getTicketById(Long id) {
//        return ticketRepository.findById(id);
//    }
//
//    @Transactional
//    public Ticket createTicket(Ticket ticket) {
//        return ticketRepository.save(ticket);
//    }
//
////    @Transactional
////    public Ticket updateTicket(Long id, Ticket ticketDetails) {
////        Ticket ticket = ticketRepository.findById(id)
////                .orElseThrow(() -> new RuntimeException("Ticket not found"));
////        ticket.setBooking(ticketDetails.getBooking());
////        ticket.setSeat(ticketDetails.getSeat());
////        ticket.setStatus(ticketDetails.getStatus());
////        return ticketRepository.save(ticket);
////    }
//
//    @Transactional
//    public void deleteTicket(Long id) {
//        ticketRepository.deleteById(id);
//    }
}

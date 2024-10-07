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
        // Check if the seat is booked
        boolean seatTaken = ticketRepository.existsBySeatIdAndScreeningId(
                ticketDTO.getSeatId(), ticketDTO.getScreeningId()
        );
        if (seatTaken) {
            throw new RuntimeException("The seat is already taken for this screening.");
        }
        Ticket ticket = ticketMapperImpl.toEntity(ticketDTO);
        ticket = ticketRepository.save(ticket);
        return ticketMapper.toDto(ticket);
    }
}

package com.movie.tkts.mappers.impl;

import com.movie.tkts.dto.ScreeningDto;
import com.movie.tkts.entities.Screening;
import com.movie.tkts.entities.Ticket;
import com.movie.tkts.dto.TicketDto;
import org.springframework.stereotype.Component;
import com.movie.tkts.mappers.IMapper;
import org.modelmapper.ModelMapper;


@Component
public class TicketMapperImpl implements IMapper<Ticket, TicketDto> {

    private final ModelMapper modelMapper;

    public TicketMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public TicketDto toDto(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        return modelMapper.map(ticket, TicketDto.class);
    }

    @Override
    public Ticket toEntity(TicketDto ticketDto) {
        if (ticketDto == null) {
            return null;
        }
        return modelMapper.map(ticketDto, Ticket.class);
    }
}

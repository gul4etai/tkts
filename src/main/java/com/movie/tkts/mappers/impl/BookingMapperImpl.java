package com.movie.tkts.mappers.impl;

import com.movie.tkts.dto.BookingDto;
import com.movie.tkts.dto.TicketDto;
import com.movie.tkts.entities.Booking;
import com.movie.tkts.mappers.IMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingMapperImpl implements IMapper<Booking, BookingDto> {

    private final ModelMapper modelMapper;
    private final TicketMapperImpl ticketMapper;

    public BookingMapperImpl(ModelMapper modelMapper, TicketMapperImpl ticketMapper) {
        this.modelMapper = modelMapper;
        this.ticketMapper = ticketMapper;

      /*  modelMapper.typeMap(Booking.class, BookingDto.class).addMappings(mapper -> {
            // Skip the bookings field in ScreeningDto to prevent recursion
            mapper.skip(BookingDto::setScreening);
        });*/
    }

    @Override
    public BookingDto toDto(Booking booking) {
        BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);

        List<TicketDto> ticketDtos = booking.getTickets().stream()
                .map(ticketMapper::toDto)
                .collect(Collectors.toList());

        bookingDto.setTickets(ticketDtos);
        return bookingDto;
    }

    @Override
    public Booking toEntity(BookingDto bookingDto) {
        if (bookingDto == null) {
            return null;
        }
        return modelMapper.map(bookingDto, Booking.class);
    }
}


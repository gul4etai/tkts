package com.movie.tkts.mappers.impl;

import com.movie.tkts.dto.BookingDto;
import com.movie.tkts.entities.Booking;
import com.movie.tkts.mappers.IMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookingMapperImpl implements IMapper<Booking, BookingDto> {

    private final ModelMapper modelMapper;

    public BookingMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public BookingDto toDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        return modelMapper.map(booking, BookingDto.class);
    }

    @Override
    public Booking toEntity(BookingDto bookingDto) {
        if (bookingDto == null) {
            return null;
        }
        return modelMapper.map(bookingDto, Booking.class);
    }
}


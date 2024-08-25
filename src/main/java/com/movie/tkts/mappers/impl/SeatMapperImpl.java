package com.movie.tkts.mappers.impl;

import com.movie.tkts.dto.SeatDto;
import com.movie.tkts.entities.Seat;
import com.movie.tkts.mappers.IMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SeatMapperImpl implements IMapper<Seat, SeatDto> {

    private final ModelMapper modelMapper;

    public SeatMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public SeatDto toDto(Seat seat) {
        if (seat == null) {
            return null;
        }
        return modelMapper.map(seat, SeatDto.class);
    }

    @Override
    public Seat toEntity(SeatDto seatDto) {
        if (seatDto == null) {
            return null;
        }
        return modelMapper.map(seatDto, Seat.class);
    }
}


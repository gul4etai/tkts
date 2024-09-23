package com.movie.tkts.mappers.impl;

import com.movie.tkts.dto.SeatDto;
import com.movie.tkts.entities.Seat;
import com.movie.tkts.mappers.IMapper;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class SeatMapperImpl implements IMapper<Seat, SeatDto> {

    private final ModelMapper modelMapper;

    public SeatMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        // Configure mapping if there are any properties to be skipped
      /*  modelMapper.typeMap(Seat.class, SeatDto.class).addMappings(mapper -> {
            // No specific properties to skip, but future changes can be made here
        });*/
    }

/*    @PostConstruct
    public void configureMappings() {
        // Explicitly map seatNum to seatNumber in SeatDto
        modelMapper.typeMap(Seat.class, SeatDto.class)
                .addMapping(Seat::getSeatNum, SeatDto::setSeatNum)
                .addMapping(Seat::getRowNum, SeatDto::setRowNum);

        // Similarly for mapping from SeatDto to Seat entity
        modelMapper.typeMap(SeatDto.class, Seat.class)
                .addMapping(SeatDto::getSeatNum, Seat::setSeatNum)
                .addMapping(SeatDto::getRowNum, Seat::setRowNum);
    }*/

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


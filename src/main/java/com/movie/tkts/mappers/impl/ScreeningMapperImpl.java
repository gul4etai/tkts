package com.movie.tkts.mappers.impl;

import com.movie.tkts.dto.ScreeningDto;
import com.movie.tkts.dto.SeatDto;
import com.movie.tkts.dto.TicketDto;
import com.movie.tkts.entities.Screening;
import com.movie.tkts.mappers.IMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScreeningMapperImpl implements IMapper<Screening, ScreeningDto> {

    private final ModelMapper modelMapper;

    public ScreeningMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        // Custom mapping to avoid recursion or handle specific fields
        // Custom mapping for Screening to ScreeningDto
      /*  modelMapper.typeMap(Screening.class, ScreeningDto.class).addMappings(mapper -> {
            mapper.map(src -> src.getMovie().getId(), ScreeningDto::setMovieId); // Map movie's id to movieId in ScreeningDto
        });*/
        // Custom mapping for Screening to ScreeningDto: skip mapping of occupied seats
        modelMapper.typeMap(Screening.class, ScreeningDto.class).addMappings(mapper -> {
            mapper.skip(ScreeningDto::setOccupiedSeats);
        });
    }



    @Override
    public ScreeningDto toDto(Screening screening) {


        if (screening == null) {
            return null;
        }

        return modelMapper.map(screening,ScreeningDto.class);
    }

    @Override
    public Screening toEntity(ScreeningDto screeningDto) {
        return modelMapper.map(screeningDto, Screening.class);
    }
}

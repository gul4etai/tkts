package com.movie.tkts.mappers.impl;

import com.movie.tkts.dto.ScreeningDto;
import com.movie.tkts.dto.SeatDto;
import com.movie.tkts.dto.TheaterDto;
import com.movie.tkts.entities.Screening;
import com.movie.tkts.entities.Theater;
import com.movie.tkts.mappers.IMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TheaterMapperImpl implements IMapper<Theater, TheaterDto> {

    private final ModelMapper modelMapper;

    public TheaterMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
//        modelMapper.typeMap(Theater.class, TheaterDto.class).addMappings(mapper -> {
//            mapper.skip(TheaterDto::setSeats);
//        });
    }

    @Override
    public TheaterDto toDto(Theater theater) {
        if (theater == null) {
            return null;
        }
        return modelMapper.map(theater, TheaterDto.class);
    }

    @Override
    public Theater toEntity(TheaterDto theaterDto) {
        if (theaterDto == null) {
            return null;
        }
        return modelMapper.map(theaterDto, Theater.class);
    }
}


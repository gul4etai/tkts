package com.movie.tkts.mappers.impl;

import com.movie.tkts.dto.ScreeningDto;
import com.movie.tkts.dto.SeatDto;
import com.movie.tkts.dto.TicketDto;
import com.movie.tkts.entities.Screening;
import com.movie.tkts.mappers.IMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.config.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScreeningMapperImpl implements IMapper<Screening, ScreeningDto> {

    private final ModelMapper modelMapper;

    public ScreeningMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        configureModelMapper();
    }

    private void configureModelMapper() {
        modelMapper.createTypeMap(Screening.class, ScreeningDto.class)
                .addMappings(mapper -> {
                    mapper.skip(ScreeningDto::setOccupiedSeats);
                });

    }

    public ScreeningDto toDto(Screening screening) {
        if (screening == null) return null;

        return modelMapper.map(screening, ScreeningDto.class);
    }

    public Screening toEntity(ScreeningDto screeningDto) {
        if (screeningDto == null) return null;

        return modelMapper.map(screeningDto, Screening.class);
    }
}


package com.movie.tkts.mappers.impl;

import com.movie.tkts.dto.ScreeningDto;
import com.movie.tkts.entities.Screening;
import com.movie.tkts.mappers.IMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ScreeningMapperImpl implements IMapper<Screening, ScreeningDto> {

    private ModelMapper modelMapper;

    public ScreeningMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ScreeningDto toDto(Screening screening) {
        return modelMapper.map(screening, ScreeningDto.class);
    }

    @Override
    public Screening toEntity(ScreeningDto screeningDto) {
        return modelMapper.map(screeningDto, Screening.class);
    }
}

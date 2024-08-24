package com.movie.tkts.mappers.impl;

import com.movie.tkts.dto.MovieDto;
import com.movie.tkts.entities.Movie;
import com.movie.tkts.mappers.IMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MovieMapperImpl implements IMapper<Movie, MovieDto> {

    private ModelMapper modelMapper;

    public MovieMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public MovieDto toDto(Movie movie) {
        return modelMapper.map(movie, MovieDto.class);
    }

    @Override
    public Movie toEntity(MovieDto movieDto) {
        return modelMapper.map(movieDto, Movie.class);
    }
}

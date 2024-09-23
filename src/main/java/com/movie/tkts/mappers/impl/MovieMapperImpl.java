package com.movie.tkts.mappers.impl;

import com.movie.tkts.dto.MovieDto;
import com.movie.tkts.dto.ScreeningDto;
import com.movie.tkts.entities.Movie;
import com.movie.tkts.entities.Screening;
import com.movie.tkts.mappers.IMapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MovieMapperImpl implements IMapper<Movie, MovieDto> {

    private final ModelMapper modelMapper;
    private final ModelMapper screeningMapper; // Inject ScreeningMapper for manual mapping

    public MovieMapperImpl(ModelMapper modelMapper, ModelMapper screeningMapper) {
        this.modelMapper = modelMapper;
        this.screeningMapper = screeningMapper;

        // Skip mapping screenings automatically to avoid recursion issues
//        modelMapper.typeMap(Movie.class, MovieDto.class).addMappings(mapper -> {
//            mapper.skip(MovieDto::setScreenings);
//        });
    }

    @Override
    public MovieDto toDto(Movie movie) {
        // Convert Movie to MovieDto
        return modelMapper.map(movie, MovieDto.class);
    }

    @Override
    public Movie toEntity(MovieDto movieDto) {
        return modelMapper.map(movieDto, Movie.class);
    }
}

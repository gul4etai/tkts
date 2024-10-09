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
    private final ScreeningMapperImpl screeningMapper;

    public MovieMapperImpl(ModelMapper modelMapper, ScreeningMapperImpl screeningMapper) {
        this.modelMapper = modelMapper;
        this.screeningMapper = screeningMapper;

        configureModelMapper();
    }

    private void configureModelMapper() {
      modelMapper.createTypeMap(MovieDto.class, Movie.class)
                .addMappings(mapper -> {
                    mapper.skip(Movie::setScreenings);
                });


    }

    public MovieDto toDto(Movie movie) {
        if (movie == null) return null;

        MovieDto movieDto = modelMapper.map(movie, MovieDto.class);

        // Handle nested screenings separately to prevent circular references
        if (movie.getScreenings() != null) {
            List<ScreeningDto> screeningDtos = movie.getScreenings().stream()
                    .map(screeningMapper::toDto)
                    .collect(Collectors.toList());
            movieDto.setScreenings(screeningDtos);
        }

        return movieDto;
    }

    public Movie toEntity(MovieDto movieDto) {
        if (movieDto == null) return null;
        Movie movie = modelMapper.map(movieDto, Movie.class);

        if (movieDto.getScreenings() != null) {
            List<Screening> screenings = movieDto.getScreenings().stream()
                    .map(screeningMapper::toEntity)
                    .collect(Collectors.toList());
            movie.setScreenings(screenings);
        }

        return movie;
    }
}


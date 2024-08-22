package com.movie.tkts.repositories;

import com.movie.tkts.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMovieRepository extends JpaRepository<Movie, Long> {
}

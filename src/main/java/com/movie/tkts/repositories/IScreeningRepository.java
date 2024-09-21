package com.movie.tkts.repositories;

import com.movie.tkts.entities.Movie;
import com.movie.tkts.entities.Screening;
import com.movie.tkts.entities.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface IScreeningRepository extends JpaRepository <Screening, Long> {

   // Optional<Screening> findById(Long id);

    List<Screening> findByDateAndTime(LocalDate date, LocalTime time);

    List<Screening> findByTheaterAndMovie(Theater theater, Movie movie);
}

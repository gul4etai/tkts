package com.movie.tkts.repositories;

import com.movie.tkts.entities.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface IScreeningRepository extends JpaRepository <Screening, Long> {
    List<Screening> findByDateAndTime(LocalDate date, LocalTime time);
}

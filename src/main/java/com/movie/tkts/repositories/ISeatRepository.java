package com.movie.tkts.repositories;

import com.movie.tkts.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISeatRepository extends JpaRepository<Seat, Long> {
}

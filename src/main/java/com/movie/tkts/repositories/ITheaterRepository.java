package com.movie.tkts.repositories;

import com.movie.tkts.entities.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITheaterRepository extends JpaRepository<Theater, Long> {
}

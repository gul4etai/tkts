package com.movie.tkts.repositories;

import com.movie.tkts.entities.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IScreeningRepository extends JpaRepository <Screening, Long> {
}

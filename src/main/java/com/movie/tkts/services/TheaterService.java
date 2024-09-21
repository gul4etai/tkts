package com.movie.tkts.services;

import com.movie.tkts.entities.Theater;
import com.movie.tkts.repositories.ITheaterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TheaterService {

    private final ITheaterRepository theaterRepository;

    public TheaterService(ITheaterRepository theaterRepository) {
        this.theaterRepository = theaterRepository;
    }

    @Transactional(readOnly = true)
    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Theater> getTheaterById(Long id) {
        return theaterRepository.findById(id);
    }

    @Transactional
    public Theater createTheater(Theater theater) {
        return theaterRepository.save(theater);
    }

    @Transactional
    public Theater updateTheater(Long id, Theater updatedTheater) {
        return theaterRepository.findById(id)
                .map(theater -> {
                    theater.setName(updatedTheater.getName());
                    theater.setRows(updatedTheater.getRows());
                    theater.setSeatsInRow(updatedTheater.getSeatsInRow());
                    return theaterRepository.save(theater);
                })
                .orElseThrow(() -> new RuntimeException("Theater not found"));
    }

    @Transactional
    public void deleteTheater(Long id) {
        if (theaterRepository.existsById(id)) {
            theaterRepository.deleteById(id);
        } else {
            throw new RuntimeException("Theater not found");
        }
    }
}

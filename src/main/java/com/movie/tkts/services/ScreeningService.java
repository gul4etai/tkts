package com.movie.tkts.services;

import com.movie.tkts.entities.Screening;
import com.movie.tkts.repositories.IScreeningRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ScreeningService {

    private final IScreeningRepository screeningRepository;

    public ScreeningService(IScreeningRepository screeningRepository) {
        this.screeningRepository = screeningRepository;
    }

    @Transactional(readOnly = true)
    public List<Screening> getAllScreenings() {
        return screeningRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Screening> getScreeningById(Long id) {
        return screeningRepository.findById(id);
    }

    @Transactional
    public Screening createScreening(Screening screening) {
        return screeningRepository.save(screening);
    }

    @Transactional
    public Screening updateScreening(Long id, Screening screeningDetails) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Screening not found"));
        screening.setDate(screeningDetails.getDate());
        screening.setTime(screeningDetails.getTime());
        screening.setMovie(screeningDetails.getMovie());
        screening.setTheater(screeningDetails.getTheater());
        return screeningRepository.save(screening);
    }

    @Transactional
    public void deleteScreening(Long id) {
        screeningRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Screening> getScreeningsByDateAndTime(LocalDate date, LocalTime time) {
        return screeningRepository.findByDateAndTime(date, time);

    }
}

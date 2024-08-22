package com.movie.tkts.entities;

import org.springframework.boot.autoconfigure.domain.EntityScan;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "booking_id")
    private Long bookingId;

    private int seats;

    @ManyToOne
    @JoinColumn(name = "screeningId")
    private Screening screening;

  /*  @ManyToOne
    @JoinColumn(name = "userId")
    private Long userId;*/
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)  // Foreign key column named 'user_id'
  private User user;
}

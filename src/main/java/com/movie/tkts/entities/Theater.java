package com.movie.tkts.entities;

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
@Table(name = "theaters")
public class Theater {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int rows;
    private int seats;
    private int capacity;

   @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   private List<Screening> screenings;  //TODO ? list from utils vs from hibernate is a "bag" no order
}

package com.movie.tkts.entities;

import jakarta.persistence.*;

import java.util.List;

import org.hibernate.annotations.Cascade;
//import org.hibernate.annotations.CascadeType;
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
    private Long theatreId;

    private int rows;
    private int seats;

   //@OneToMany(mappedBy = "theatre")
    /*@OneToMany
    @JoinColumn (name = "theater")*/
   @OneToMany(mappedBy = "theater", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
   private List<Screening> screenings;  //TODO ? list from utils vs from hibernate is a "bag" no order
}

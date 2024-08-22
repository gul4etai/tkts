package com.movie.tkts;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Log
public class TktsApp {

	public static void main(String[] args) {

		SpringApplication.run(TktsApp.class, args);
	}

}
